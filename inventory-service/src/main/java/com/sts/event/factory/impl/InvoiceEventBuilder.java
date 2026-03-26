package com.sts.event.factory.impl;

import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventAbstractFactory;
import com.sts.utils.enums.StockUpdateSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Component
public class InvoiceEventBuilder implements StockUpdateEventAbstractFactory {

    @Override
    public StockUpdateEvent build(Object input) {

        InvoiceInput invoiceInput = (InvoiceInput) input;
        var items = Arrays.stream(invoiceInput.resolvedItems).map(item -> new StockUpdateEvent.StockUpdateItem(
                        item.variantId(),
                        item.unitId(),
                        item.quantity(),
                        StockUpdateSource.SALE))
                .toList();

        return new StockUpdateEvent(null, invoiceInput.invoiceId, items);
    }

    public record ResolvedInvoiceItem(
            UUID variantId,
            UUID unitId,
            UUID invoiceId,
            BigDecimal quantity) {
    }

    public record InvoiceInput(
            UUID invoiceId,
            ResolvedInvoiceItem[] resolvedItems
    ) {
    }


}
