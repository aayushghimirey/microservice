package com.sts.event.resolver;

import com.sts.client.MenuGateway;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.event.StockUpdateEvent;
import com.sts.event.InvoiceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class InvoiceStockUpdateResolver {

    private final MenuGateway menuGateway;
    private final StockUpdateEventFactory stockUpdateEventFactory;

    public StockUpdateEvent resolve(InvoiceEvent event) {
        var resolvedItems = new ArrayList<StockUpdateEventFactory.ResolvedInvoiceItem>();

        for (var item : event.getItems()) {
            var ingredients = menuGateway.getIngredientsOrThrow(item.getMenuId());
            BigDecimal orderQuantity = BigDecimal.valueOf(item.getQuantity());

            for (var ingredient : ingredients) {
                BigDecimal deductionQuantity = BigDecimal.valueOf(ingredient.getQuantity()).multiply(orderQuantity);
                resolvedItems.add(
                        new StockUpdateEventFactory.ResolvedInvoiceItem(
                                ingredient.getVariantId(),
                                ingredient.getUnitId(),
                                deductionQuantity
                        )
                );
            }
        }

        return stockUpdateEventFactory.buildFromInvoiceItems(event.getInvoiceId(), resolvedItems);
    }
}
