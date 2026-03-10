package com.sts.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sts.utils.enums.StockUpdateSource;

public record StockUpdateEvent(
                UUID purchaseId,
                UUID invoiceId,
                List<Item> items) {

        public record Item(
                        UUID variantId,
                        UUID unitId,
                        BigDecimal quantity,
                        StockUpdateSource source

        ) {
        }

}
