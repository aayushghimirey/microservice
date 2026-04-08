package com.sts.event.factory.impl;

import com.sts.event.InvoiceEvent;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.utils.enums.TransactionReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InvoiceEventFactory implements StockUpdateEventFactory<InvoiceEvent> {

    private static final Logger log = LoggerFactory.getLogger(InvoiceEventFactory.class);

    @Override
    public StockUpdateEvent build(InvoiceEvent input) {

        log.info("Building StockUpdateEvent from InvoiceEvent with invoiceId: {}", input.getInvoiceId());

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
                input.getTenantId(),
                TransactionReference.SALES,
                stockItems
        );
    }


}
