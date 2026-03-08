package com.sts.event;


import com.sts.model.purchase.Purchase;
import com.sts.utils.enums.StockUpdateSource;
import org.springframework.stereotype.Component;
import com.sts.event.StockUpdateEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/*
 * Application event for updating stock items after purchase / adjustment
 * */
@Component
public class StockUpdateEventBuilder {

    public StockUpdateEvent buildFromPurchase(Purchase purchase) {

        var infos = purchase.getPurchaseItems()
                .stream()
                .map(item -> new StockUpdateEvent.Info(
                        item.getVariantId(),
                        item.getUnitId(),
                        item.getQuantity(),
                        StockUpdateSource.PURCHASE
                ))
                .toList();

        return new StockUpdateEvent(purchase.getId(), infos);
    }


    public StockUpdateEvent buildFromAdjustment(
            UUID variantId,
            UUID unitId,
            BigDecimal quantity
    ) {

        var info = new StockUpdateEvent.Info(
                variantId,
                unitId,
                quantity,
                StockUpdateSource.ADJUSTMENT
        );

        return new StockUpdateEvent(
                null,
                List.of(info)
        );
    }


}
