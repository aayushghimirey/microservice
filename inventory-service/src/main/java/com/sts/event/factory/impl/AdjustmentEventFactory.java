package com.sts.event.factory.impl;

import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventAbstractFactory;
import com.sts.utils.enums.StockUpdateSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class AdjustmentEventFactory implements StockUpdateEventAbstractFactory {

    public record AdjustmentInput(UUID variantId, UUID unitId, BigDecimal quantity) {
    }

    @Override
    public StockUpdateEvent build(Object input) {
        AdjustmentInput adj = (AdjustmentInput) input;

        return new StockUpdateEvent(
                null,
                null,
                List.of(new StockUpdateEvent.StockUpdateItem(
                        adj.variantId,
                        adj.unitId,
                        adj.quantity,
                        StockUpdateSource.ADJUSTMENT)));
    }
}
