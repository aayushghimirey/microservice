package com.sts.service.stock;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.event.StockUpdateEvent;
import com.sts.event.StockUpdateEventBuilder;
import com.sts.exception.DuplicateStock;
import com.sts.exception.StockNotFound;
import com.sts.exception.UnitNotFound;
import com.sts.exception.VariantNotFound;
import com.sts.mapper.StockMapper;
import com.sts.mapper.StockUpdateMapper;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.utils.contant.AppConstants;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final StockUpdateMapper stockUpdateMapper;
    private final StockVariantRepository stockVariantRepository;
    private final StockUpdateEventBuilder stockUpdateEventBuilder;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Transactional
    public StockResponse createStock(CreateStockCommand command) {
        if (stockRepository.exitsByName(command.name())) {
            throw new DuplicateStock(String.format(AppConstants.STOCK_ALREADY_EXISTS, command.name()));
        }
        Stock stock = stockMapper.buildStock(command);

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    @Transactional
    public StockResponse updateStock(UUID stockId, UpdateStockCommand updateCommand) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(
                () -> new StockNotFound(String.format(AppConstants.STOCK_NOT_FOUND, stockId))
        );
        stockUpdateMapper.updateStock(stock, updateCommand);
        return stockMapper.toResponse(stockRepository.save(stock));
    }


    @Transactional
    public void createAdjustmentStock(StockAdjustmentCommand command) {
        StockVariant stockVariant = stockVariantRepository.findById(command.variantId()).orElseThrow(
                () -> new VariantNotFound(String.format(AppConstants.VARIANT_NOT_FOUND, command.variantId()))
        );

        VariantUnit variantUnit = stockVariant.getUnits().stream()
                .filter(unit -> unit.getId().equals(command.unitId()))
                .findFirst()
                .orElseThrow(() -> new UnitNotFound(String.format(AppConstants.UNIT_NOT_FOUND, command.unitId())));

        stockUpdateMapper.adjustStock(stockVariant, variantUnit, command);

        StockUpdateEvent stockUpdateEvent = stockUpdateEventBuilder.buildStockUpdateEventFromAdjustment(
                stockVariant.getId(),
                variantUnit.getId(),
                command.quantity(),
                command.reason()
        );
        stockVariantRepository.save(stockVariant);

        applicationEventPublisher.publishEvent(stockUpdateEvent);

    }

}
