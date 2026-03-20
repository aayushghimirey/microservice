package com.sts.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.exception.DuplicateResourceException;
import com.sts.mapper.StockMapper;
import com.sts.mapper.StockUpdateMapper;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockTransaction;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockRepository;
import com.sts.repository.StockTransactionRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.service.StockService;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.service.resolver.VariantUnitResolver;
import com.sts.service.specification.StockSpecification;
import com.sts.shared.event.DomainEventPublisher;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.StockUpdateSource;
import com.sts.utils.enums.TransactionReference;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockVariantRepository stockVariantRepository;
    private final StockTransactionRepository stockTransactionRepository;

    private final StockMapper stockMapper;
    private final StockUpdateMapper stockUpdateMapper;

    private final StockUpdateEventFactory stockUpdateEventFactory;
    private final StockSpecification specification;

    private final DomainEventPublisher eventPublisher;
    private final VariantUnitResolver variantUnitResolver;
    private final ReferenceResolver referenceResolver;

    @Override
    @Transactional
    public StockResponse createStock(CreateStockCommand command) {
        log.info(AppConstants.LOG_MESSAGES.CREATING_STOCK, command.name());
        checkStockNameUniqueness(command.name());

        Stock stock = stockRepository.save(stockMapper.buildStock(command));

        return stockMapper.toResponse(stock);
    }

    @Override
    @Transactional
    public StockResponse updateStock(UUID stockId, UpdateStockCommand updateCommand) {
        log.info(AppConstants.LOG_MESSAGES.UPDATING_STOCK, stockId);
        checkStockNameUniquenessForUpdate(stockId, updateCommand.name());

        Stock stock = referenceResolver.getStockOrThrow(stockId);
        stockUpdateMapper.updateStock(stock, updateCommand);

        Stock updatedStock = stockRepository.save(stock);

        return stockMapper.toResponse(updatedStock);
    }

    @Override
    @Transactional
    public void adjustStock(StockAdjustmentCommand command) {
        log.info(AppConstants.LOG_MESSAGES.ADJUSTING_STOCK, command.variantId(), command.unitId(), command.quantity());
        StockVariant stockVariant = referenceResolver.getVariantOrThrow(command.variantId());
        VariantUnit variantUnit = variantUnitResolver
                .getVariantUnitOrThrow(stockVariant.getId(), command.unitId());

        publishStockUpdate(stockVariant.getId(), variantUnit.getId(), command.quantity());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse> getAllStock(Pageable pageable) {
        return stockRepository.findAll(pageable)
                .map(stockMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable) {
        Specification<Stock> spec = specification.buildSpecification(queryRequest);

        return stockRepository.findAll(spec, pageable)
                .map(stockMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse.VariantResponse> getAllVariantByStockId(UUID stockId, Pageable pageable) {
        return stockVariantRepository.findAllByStockId(stockId, pageable)
                .map(stockMapper::toVariantResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByVariantIdAndUnitId(UUID variantId, UUID unitId) {
        log.info(AppConstants.LOG_MESSAGES.VALIDATING_VARIANT, variantId, unitId);
        return stockVariantRepository.existsByIdAndUnits_Id(variantId, unitId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processStockUpdates(StockUpdateEvent event) {

        UUID referenceId = resolveReferenceId(event);

        for (var item : event.items()) {
            processStockUpdateItem(item, referenceId);
        }
    }

    // -------------------- Helpers ----------------------

    private void processStockUpdateItem(StockUpdateEvent.StockUpdateItem item, UUID referenceId) {
        StockVariant variant = referenceResolver.getVariantOrThrow(item.variantId());
        VariantUnit unit = variantUnitResolver.getVariantUnitOrThrow(item.variantId(), item.unitId());

        BigDecimal stockDelta = calculateStockDelta(item.quantity(), unit.getConversionRate(), item.source());
        log.info(AppConstants.LOG_MESSAGES.CALCULATING_STOCK_DELTA, stockDelta, item.variantId(), item.source());
        BigDecimal updatedBalance = variant.getCurrentStock().add(stockDelta);

        variant.setCurrentStock(updatedBalance);
        stockVariantRepository.save(variant);

        StockTransaction transaction = buildTransaction(item, referenceId, stockDelta, updatedBalance);
        log.info(AppConstants.LOG_MESSAGES.CREATING_STOCK_TRANSACTION, item.variantId(), updatedBalance);
        stockTransactionRepository.save(transaction);
    }

    private BigDecimal calculateStockDelta(BigDecimal quantity, BigDecimal conversionRate, StockUpdateSource source) {
        BigDecimal delta = quantity.multiply(conversionRate);
        boolean isAddition = source == StockUpdateSource.PURCHASE || source == StockUpdateSource.RETURN;
        return isAddition ? delta : delta.negate();
    }

    private StockTransaction buildTransaction(
            StockUpdateEvent.StockUpdateItem item,
            UUID referenceId,
            BigDecimal stockDelta,
            BigDecimal balanceAfter) {
        return StockTransaction.builder()
                .variantId(item.variantId())
                .unitId(item.unitId())
                .quantityChange(stockDelta)
                .balanceAfter(balanceAfter)
                .referenceId(referenceId)
                .referenceType(mapReferenceType(item.source()))
                .remark(item.source().name())
                .build();
    }

    private UUID resolveReferenceId(StockUpdateEvent event) {
        return event.purchaseId() != null ? event.purchaseId() : event.invoiceId();
    }

    private TransactionReference mapReferenceType(StockUpdateSource source) {
        return switch (source) {
            case PURCHASE -> TransactionReference.PURCHASE;
            case SALE -> TransactionReference.SALES;
            case ADJUSTMENT, RETURN -> TransactionReference.ADJUSTMENT;
        };
    }

    private void checkStockNameUniqueness(String name) {
        if (name == null || name.isBlank()) {
            return;
        }

        if (stockRepository.existsByName(name)) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.STOCK_ALREADY_EXISTS, name));
        }
    }

    private void checkStockNameUniquenessForUpdate(UUID stockId, String name) {
        if (name == null || name.isBlank()) {
            return;
        }

        if (stockRepository.existsByNameAndIdNot(name, stockId)) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.STOCK_ALREADY_EXISTS, name));
        }
    }

    private void publishStockUpdate(UUID variantId, UUID unitId, BigDecimal quantity) {
        log.info(AppConstants.LOG_MESSAGES.PUBLISHING_ADJUSTMENT_EVENT, variantId);
        StockUpdateEvent event = stockUpdateEventFactory.buildFromAdjustment(variantId, unitId, quantity);
        eventPublisher.publish(event);
    }
}