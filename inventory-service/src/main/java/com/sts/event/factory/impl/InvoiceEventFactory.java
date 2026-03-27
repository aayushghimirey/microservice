package com.sts.event.factory.impl;

import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.utils.enums.StockUpdateSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Component
public class InvoiceEventFactory implements StockUpdateEventFactory<InvoiceEventFactory.InvoiceInput> {

    @Override
    public StockUpdateEvent build(InvoiceInput input) {

        var items = Arrays.stream(input.resolvedItems)
                .map(item -> new StockUpdateEvent.StockUpdateItem(
                        item.variantId(),
                        item.unitId(),
                        item.quantity(),
                        StockUpdateSource.SALE))
                .toList();

        return new StockUpdateEvent(null, input.invoiceId(), items);
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
