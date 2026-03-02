package com.sts.stock.application.usecase;

import com.sts.stock.application.event.StockUpdateEvent;
import com.sts.stock.application.event.StockUpdateEventBuilder;
import com.sts.stock.command.*;
import com.sts.stock.domain.model.Stock;
import com.sts.stock.domain.model.StockVariant;
import com.sts.stock.domain.model.VariantUnit;
import com.sts.stock.domain.repository.StockRepository;
import com.sts.stock.domain.repository.StockVariantRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class StockCommandUseCase {

    private final StockRepository stockRepository;
    private final StockCommandHandler stockCommandHandler;
    private final StockUpdateHandler stockUpdateHandler;
    private final StockVariantRepository stockVariantRepository;
    private final StockUpdateEventBuilder stockUpdateEventBuilder;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Transactional
    public Stock createStock(CreateStockCommand command) {
        Stock stock = stockCommandHandler.buildStock(command);
        return stockRepository.save(stock);
    }

    @Transactional
    public Stock updateStock(UUID stockId, UpdateStockCommand updateCommand) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid stock id")
        );
        stockUpdateHandler.updateStock(stock, updateCommand);
        return stockRepository.save(stock);
    }


    @Transactional
    public void createAdjustmentStock(StockAdjustmentCommand command) {
        StockVariant stockVariant = stockVariantRepository.findById(command.variantId()).orElseThrow(
                () -> new ResourceNotFoundException("Invalid stock variant id")
        );

        VariantUnit variantUnit = stockVariant.getUnits().stream()
                .filter(unit -> unit.getId().equals(command.unitId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invalid unit id for the given stock variant"));

        stockUpdateHandler.adjustStock(stockVariant, variantUnit, command);

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
