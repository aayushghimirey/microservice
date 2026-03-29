package com.sts.event.factory.impl;

import com.sts.event.InvoiceEvent;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.utils.enums.StockUpdateSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class InvoiceEventFactory implements StockUpdateEventFactory<InvoiceEvent> {

    @Override
    public StockUpdateEvent build(InvoiceEvent input) {

        List<StockUpdateEvent.StockUpdateItem> stockItems = input.getItems().stream()
                .flatMap(item -> item.getIngredients().stream()
                        .map(ingredient -> new StockUpdateEvent.StockUpdateItem(
                                ingredient.getVariantId(),
                                ingredient.getUnitId(),
                                BigDecimal.valueOf(ingredient.getQuantity())
                                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                        ))
                )
                .toList();

        return new StockUpdateEvent(
                null,
                input.getInvoiceId(),
                stockItems
        );
    }

    public record ResolvedInvoiceItem(
            UUID variantId,
            UUID unitId,
            UUID invoiceId,
            BigDecimal quantity) {
    }


}
