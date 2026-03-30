package com.sts.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sts.utils.enums.StockUpdateSource;
import com.sts.utils.enums.TransactionReference;

public record StockUpdateEvent(
        UUID purchaseId,
        UUID invoiceId,
        TransactionReference transactionReference,
        List<StockUpdateItem> items) {

    public record StockUpdateItem(
            UUID variantId,
            UUID unitId,
            BigDecimal quantity

    ) {
    }

}
