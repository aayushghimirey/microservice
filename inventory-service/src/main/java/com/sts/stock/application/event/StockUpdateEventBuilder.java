package com.sts.stock.application.event;

import com.sts.purchase.domain.model.Purchase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class StockUpdateEventBuilder {

    public StockUpdateEvent buildStockUpdateEventFromPurchase(Purchase purchase) {
        List<StockUpdateEvent.Info> infoList = purchase.getPurchaseItems().stream()
                .map(item -> {
                    return new StockUpdateEvent.Info(
                            item.getVariantId(),
                            item.getUnitId(),
                            item.getQuantity(),
                            "PURCHASE"
                    );
                })
                .toList();

        return new StockUpdateEvent(purchase.getId(), infoList);
    }

    public StockUpdateEvent buildStockUpdateEventFromAdjustment(UUID variantId, UUID unitId, BigDecimal quantity, String reason) {
        StockUpdateEvent.Info info = new StockUpdateEvent.Info(variantId, unitId, quantity, reason);
        return new StockUpdateEvent(null, List.of(info));
    }

}
