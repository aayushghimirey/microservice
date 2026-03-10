package com.sts.service.impl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.event.StockUpdateEventFactory;
import com.sts.exception.DuplicateResourceException;
import com.sts.mapper.StockMapper;
import com.sts.mapper.StockUpdateMapper;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.service.StockService;
import com.sts.service.specification.StockSpecification;
import com.sts.support.ReferenceResolver;
import com.sts.support.VariantUnitResolver;
import com.sts.support.event.DomainEventPublisher;
import com.sts.utils.constant.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockVariantRepository stockVariantRepository;

    private final StockMapper stockMapper;
    private final StockUpdateMapper stockUpdateMapper;

    private final StockUpdateEventFactory stockUpdateEventFactory;
    private final StockSpecification specification;

    private final DomainEventPublisher eventPublisher;
    private final VariantUnitResolver variantUnitResolver;
    private final ReferenceResolver referenceResolver;

    // ----------- COMMANDS ----------------

    @Override
    @Transactional
    public StockResponse createStock(CreateStockCommand command) {

        if (stockRepository.existsByName(command.name())) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.STOCK_ALREADY_EXISTS, command.name()));
        }

        log.info("Creating new stock with name: {}", command.name());
        Stock stock = stockRepository.save(
                stockMapper.buildStock(command));

        return stockMapper.toResponse(stock);
    }

    @Override
    @Transactional
    public StockResponse updateStock(UUID stockId, UpdateStockCommand updateCommand) {

        Stock stock = referenceResolver.getStockOrThrow(stockId);

        stockUpdateMapper.updateStock(stock, updateCommand);

        return stockMapper.toResponse(stock);
    }

    @Override
    @Transactional
    public void adjustStock(StockAdjustmentCommand command) {
        log.info("Adjusting stock variant {} for unit {}", command.variantId(), command.unitId());

        StockVariant stockVariant = referenceResolver.getVariantOrThrow(command.variantId());

        VariantUnit variantUnit = variantUnitResolver
                .getVariantUnitOrThrow(stockVariant.getId(), command.unitId());

        publishStockUpdate(stockVariant.getId(), variantUnit.getId(), command.quantity());
    }

    // ----------- QUERIES ----------------

    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse> getAllStock(Pageable pageable) {
        return stockRepository
                .findAll(pageable)
                .map(stockMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable) {

        Specification<Stock> spec = specification.buildSpecification(queryRequest);

        return stockRepository
                .findAll(spec, pageable)
                .map(stockMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse.VariantResponse> getAllVariantByStockId(UUID stockId, Pageable pageable) {
        return stockVariantRepository
                .findAllByStockId(stockId, pageable)
                .map(stockMapper::toVariantResponse);
    }

    // ----------- VALIDATION ----------------

    @Transactional(readOnly = true)
    public boolean existsByVariantIdAndUnitId(UUID variantId, UUID unitId) {
        Optional<StockVariant> stockVariant = stockVariantRepository.findById(variantId)
                .filter(variant -> variant.getUnits().stream()
                        .anyMatch(u -> u.getId().equals(unitId)));
        return stockVariant.isPresent();
    }

    // ----------- EVENTS ----------------

    private void publishStockUpdate(UUID variantId, UUID unitId, BigDecimal quantity) {

        eventPublisher.publish(
                stockUpdateEventFactory.buildFromAdjustment(
                        variantId,
                        unitId,
                        quantity));
    }
}