package com.sts.dto.request;

import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;

import java.util.UUID;

public record GetPurchaseQueryRequest(
        UUID vendorId,
        String invoiceNumber,
        BillingType billingType,
        MoneyTransaction moneyTransaction
) {
}
