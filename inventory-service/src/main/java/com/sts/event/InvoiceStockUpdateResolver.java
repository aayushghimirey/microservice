package com.sts.event;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceStockUpdateResolver {

    private final MenuGateway menuGateway;
    private final StockUpdateEventFactory stockUpdateEventFactory;

    public StockUpdateEvent resolve(InvoiceEvent event) {
        List<StockUpdateEventFactory.ResolvedInvoiceItem> resolvedItems = new ArrayList<>();

        for (var item : event.getItems()) {
            var ingredients = menuGateway.getIngredientsOrThrow(item.getMenuId());
            double orderQuantity = item.getQuantity();

            for (var ingredient : ingredients) {
                double deductionQuantity = ingredient.getQuantity() * orderQuantity;
                resolvedItems.add(
                        new StockUpdateEventFactory.ResolvedInvoiceItem(
                                ingredient.getVariantId(),
                                ingredient.getUnitId(),
                                BigDecimal.valueOf(deductionQuantity)
                        )
                );
            }
        }

        return stockUpdateEventFactory.buildFromInvoiceItems(event.getInvoiceId(), resolvedItems);
    }
}