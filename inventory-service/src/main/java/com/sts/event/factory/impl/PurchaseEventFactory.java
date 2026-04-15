package com.sts.event.factory.impl;

import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.model.purchase.Purchase;

import com.sts.utils.enums.TransactionReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseEventFactory implements StockUpdateEventFactory<Purchase> {

    @Override
    public StockUpdateEvent build(Purchase purchase) {
        List<StockUpdateEvent.StockUpdateItem> items = purchase.getPurchaseItems().stream()
                .map(item -> new StockUpdateEvent.StockUpdateItem(
                        item.getVariantId(),
                        item.getUnitId(),
                        item.getQuantity()))
                .toList();

        return new StockUpdateEvent(purchase.getId(), null, purchase.getTenantId(), TransactionReference.PURCHASE, true, items);
    }


}