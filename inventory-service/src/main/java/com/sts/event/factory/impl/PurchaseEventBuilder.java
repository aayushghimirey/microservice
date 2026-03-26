package com.sts.event.factory.impl;

import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateEventAbstractFactory;
import com.sts.model.purchase.Purchase;
import com.sts.utils.enums.StockUpdateSource;
import org.springframework.stereotype.Component;

@Component
public class PurchaseEventBuilder implements StockUpdateEventAbstractFactory {


    @Override
    public StockUpdateEvent build(Object input) {

        Purchase purchase = (Purchase) input;

        var infos = purchase.getPurchaseItems().stream()
                .map(item -> new StockUpdateEvent.StockUpdateItem(
                        item.getVariantId(),
                        item.getUnitId(),
                        item.getQuantity(),
                        StockUpdateSource.PURCHASE))
                .toList();

        return new StockUpdateEvent(purchase.getId(), null, infos);
    }
}
