package com.sts.event.factory.impl;

import com.sts.event.InvoiceEvent;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.utils.enums.TransactionReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class InvoiceEventFactory implements StockUpdateEventFactory<InvoiceEvent> {


    @Override
    public StockUpdateEvent build(InvoiceEvent input) {

        log.info("Building StockUpdateEvent from InvoiceEvent with invoiceId: {} and tenantId {}", input.getInvoiceId(), input.getTenantId());

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
                false,
                stockItems
        );
    }


}
