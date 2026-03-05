package com.sts.dto.request;

import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreatePurchaseCommand(
        String invoiceNumber,
        BillingType billingType,
        MoneyTransaction moneyTransaction,
        BigDecimal discountAmount,
        List<PurchaseItemCommand> items
) {

    public record PurchaseItemCommand(
            UUID variantId,
            UUID unitId,
            BigDecimal quantity,
            BigDecimal perUnitPrice,
            BigDecimal discountAmount
    ) {
    }
}