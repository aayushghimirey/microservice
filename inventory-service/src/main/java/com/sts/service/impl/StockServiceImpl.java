package com.sts.service.impl;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.event.StockUpdateEvent;
import com.sts.event.StockUpdateEventBuilder;
import com.sts.exception.DuplicateResourceException;
import com.sts.exception.ResourceNotFoundException;
import com.sts.mapper.StockMapper;
import com.sts.mapper.StockUpdateMapper;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.service.specification.StockSpecification;
import com.sts.service.StockService;
import com.sts.utils.contant.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StockServiceImpl implements StockService {


    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final StockUpdateMapper stockUpdateMapper;
    private final StockVariantRepository stockVariantRepository;
    private final StockUpdateEventBuilder stockUpdateEventBuilder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final StockSpecification specification;

    @Transactional
    public StockResponse createStock(CreateStockCommand command) {
        if (stockRepository.existsByName(command.name())) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.STOCK_ALREADY_EXISTS, command.name()));
        }
        Stock stock = stockMapper.buildStock(command);

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    @Transactional
    public StockResponse updateStock(UUID stockId, UpdateStockCommand updateCommand) {

        Stock stock = stockRepository.findById(stockId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format(AppConstants.ERROR_MESSAGES.STOCK_NOT_FOUND, stockId)));

        stockUpdateMapper.updateStock(stock, updateCommand);
        return stockMapper.toResponse(stock);
    }

    @Transactional
    public void adjustStock(StockAdjustmentCommand command) {

        StockVariant stockVariant = stockVariantRepository.findById(command.variantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(AppConstants.ERROR_MESSAGES.VARIANT_NOT_FOUND, command.variantId())));

        VariantUnit variantUnit = stockVariant.getUnits().stream()
                .filter(unit -> unit.getId().equals(command.unitId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(AppConstants.ERROR_MESSAGES.UNIT_NOT_FOUND, command.unitId())));

        stockUpdateMapper.adjustStock(stockVariant, variantUnit, command);

        stockVariantRepository.save(stockVariant);

        publishAdjustmentEvent(stockVariant, variantUnit, command);
    }


    // -- query -----------------
    @Transactional(readOnly = true)
    public Page<StockResponse> getAllStock(Pageable pageable) {
        return stockRepository.findAll(pageable).map(stockMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<StockResponse> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable) {
        Specification<Stock> spec = specification.buildSpecification(queryRequest);
        return stockRepository.findAll(spec, pageable).map(stockMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<StockResponse.VariantResponse> getAllVariantByStockId(UUID stockId, Pageable pageable) {
        return stockVariantRepository.findAllByStockId(stockId, pageable).map(stockMapper::toVariantResponse);
    }

    // id validators
    @Transactional(readOnly = true)
    public boolean validateVariantIdWithUnitId(UUID variantId, UUID unitId) {
        Optional<StockVariant> stockVariant = stockVariantRepository.findById(variantId)
                .filter(variant -> variant.getUnits().stream()
                        .anyMatch(u -> u.getId().equals(unitId)));
        return stockVariant.isPresent();
    }


    // -- Private helper
    private void publishAdjustmentEvent(StockVariant variant, VariantUnit unit, StockAdjustmentCommand command) {

        StockUpdateEvent event = stockUpdateEventBuilder.buildFromAdjustment(
                variant.getId(),
                unit.getId(),
                command.quantity()
        );

        applicationEventPublisher.publishEvent(event);
    }

}
