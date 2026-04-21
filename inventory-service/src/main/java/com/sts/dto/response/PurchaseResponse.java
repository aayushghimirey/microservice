package com.sts.dto.response;

import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PurchaseResponse(
        UUID id,
        String invoiceNumber,
        BillingType billingType,
        MoneyTransaction moneyTransaction,
        BigDecimal discountAmount,
        BigDecimal subTotal,
        BigDecimal vatAmount,
        BigDecimal grossTotal,
        String vendorName,
        UUID vendorId,
        List<PurchaseItemResponse> items
) {

    public record PurchaseItemResponse(
            UUID variantId,
            UUID unitId,
            BigDecimal quantity,
            BigDecimal perUnitPrice,
            BigDecimal discountAmount,
            BigDecimal subTotal,
            BigDecimal netTotal
    ) {
    }
}