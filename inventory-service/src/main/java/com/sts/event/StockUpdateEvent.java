package com.sts.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sts.utils.enums.StockUpdateSource;
import com.sts.utils.enums.TransactionReference;

public record StockUpdateEvent(
        UUID purchaseId,
        UUID invoiceId,
        UUID tenantId,
        TransactionReference transactionReference,
        Boolean isAddition,
        List<StockUpdateItem> items
) {

    public record StockUpdateItem(
            UUID variantId,
            UUID unitId,
            BigDecimal quantity

    ) {
    }

}
