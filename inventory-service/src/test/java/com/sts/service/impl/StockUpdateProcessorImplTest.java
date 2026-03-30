package com.sts.service.impl;


import com.sts.event.StockUpdateEvent;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockTransactionRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;
import com.sts.shared.StockOutboxPublisher;

import com.sts.utils.enums.TransactionReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import java.util.List;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StockUpdateProcessorImplTest {

    @Mock
    StockVariantRepository stockVariantRepository;
    @Mock
    VariantUnitRepository variantUnitRepository;
    @Mock
    StockTransactionRepository stockTransactionRepository;
    @Mock
    StockOutboxPublisher stockOutboxPublisher;

    @InjectMocks
    StockUpdateProcessorImpl stockUpdateProcessor;


    UUID purchaseId = UUID.randomUUID();
    UUID variantId = UUID.randomUUID();
    UUID unitId = UUID.randomUUID();

    Stock stock;
    StockVariant variant;
    VariantUnit unit;
    StockUpdateEvent event;

    @BeforeEach
    void setUpPurchase() {

        stock = new Stock();

        variant = new StockVariant();
        variant.setId(variantId);
        variant.setStock(stock);
        variant.setCurrentStock(BigDecimal.valueOf(100));

        unit = new VariantUnit();
        unit.setId(unitId);
        unit.setConversionRate(BigDecimal.valueOf(2));

        event = new StockUpdateEvent(
                purchaseId,
                null,
                TransactionReference.PURCHASE,
                List.of(new StockUpdateEvent.StockUpdateItem(
                        variantId, unitId, BigDecimal.valueOf(5)
                ))
        );
    }


    @Test
    void shouldIncreaseStock() {

        when(stockVariantRepository.findAllById(anySet())).thenReturn(List.of(variant));
        when(variantUnitRepository.findAllById(anySet())).thenReturn(List.of(unit));

        stockUpdateProcessor.process(event);

        assertEquals(BigDecimal.valueOf(110), variant.getCurrentStock());
        verify(stockVariantRepository).saveAll(List.of(variant));
        verify(stockTransactionRepository).saveAll(anyList());

    }


}