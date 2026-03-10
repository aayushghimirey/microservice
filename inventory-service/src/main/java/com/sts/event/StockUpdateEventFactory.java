package com.sts.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sts.model.purchase.Purchase;
import com.sts.utils.enums.StockUpdateSource;

@Component
public class StockUpdateEventFactory {

        public StockUpdateEvent buildFromPurchase(Purchase purchase) {
                var infos = purchase.getPurchaseItems().stream()
                                .map(item -> new StockUpdateEvent.Item(
                                                item.getVariantId(),
                                                item.getUnitId(),
                                                item.getQuantity(),
                                                StockUpdateSource.PURCHASE))
                                .toList();

                return new StockUpdateEvent(purchase.getId(), null, infos);
        }

        public StockUpdateEvent buildFromAdjustment(UUID variantId, UUID unitId, BigDecimal quantity) {
                return new StockUpdateEvent(
                                null,
                                null,
                                List.of(new StockUpdateEvent.Item(
                                                variantId,
                                                unitId,
                                                quantity,
                                                StockUpdateSource.ADJUSTMENT)));
        }
}