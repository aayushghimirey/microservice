package com.sts.event.resolver;

import com.sts.client.MenuGateway;
import com.sts.event.MenuIngredientResponse;
import com.sts.event.StockUpdateEvent;
import com.sts.event.InvoiceEvent;
import com.sts.event.factory.StockUpdateFactoryRegistry;
import com.sts.event.factory.impl.InvoiceEventFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceStockUpdateResolver {

    private final MenuGateway menuGateway;
    private final StockUpdateFactoryRegistry factoryRegistry;

    public StockUpdateEvent resolve(InvoiceEvent event) {
//
//        List<InvoiceEventFactory.ResolvedInvoiceItem> resolvedItems = new ArrayList<>();
//
//        Set<UUID> menuIds = event.getItems().stream()
//                .map(InvoiceEvent.InvoiceMenuItem::getMenuId)
//                .collect(Collectors.toSet());
//
//        Map<UUID, List<MenuIngredientResponse>> menuIngredientsMap =
//                menuGateway.getIngredientsBatch(menuIds);
//
//        for (var item : event.getItems()) {
//
//
//            BigDecimal orderQty = BigDecimal.valueOf(item.getQuantity());
//
//            for (var ingredient : ingredients) {
//
//                BigDecimal deductionQty =
//                        BigDecimal.valueOf(ingredient.getQuantity()).multiply(orderQty);
//
//                resolvedItems.add(
//                        new InvoiceEventFactory.ResolvedInvoiceItem(
//                                ingredient.getVariantId(),
//                                ingredient.getUnitId(),
//                                event.getInvoiceId(),
//                                deductionQty
//                        )
//                );
//            }
//        }
//
//        return factoryRegistry.forInvoice(
//                new InvoiceEventFactory.InvoiceInput(
//                        event.getInvoiceId(),
//                        resolvedItems.toArray(new InvoiceEventFactory.ResolvedInvoiceItem[0])
//                )
//        );

        return null;
    }
}