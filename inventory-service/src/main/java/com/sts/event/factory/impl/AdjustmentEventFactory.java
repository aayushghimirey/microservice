package com.sts.event.factory.impl;

import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.utils.enums.StockUpdateSource;
import com.sts.utils.enums.TransactionReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class AdjustmentEventFactory implements StockUpdateEventFactory<AdjustmentEventFactory.AdjustmentInput> {


    public record AdjustmentInput(UUID variantId, UUID unitId, UUID tenantId, Boolean isAddition, BigDecimal quantity) {
    }

    @Override
    public StockUpdateEvent build(AdjustmentInput input) {
        return new StockUpdateEvent(
                null,
                null,
                input.tenantId,
                TransactionReference.ADJUSTMENT,
                input.isAddition,
                List.of(new StockUpdateEvent.StockUpdateItem(
                        input.variantId,
                        input.unitId,
                        input.quantity
                )));
    }
}
