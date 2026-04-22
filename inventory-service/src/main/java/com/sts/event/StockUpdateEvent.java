package com.sts.event;

import com.sts.utils.enums.TransactionReference;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


// core class for making update in stock
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
