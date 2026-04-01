package com.sts.dto;


import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PurchaseRecordResponse(
        UUID id,
        UUID purchaseId,
        BillingType billingType,
        MoneyTransaction moneyTransaction,
        BigDecimal vatAmount,
        BigDecimal grossTotal
) {
}
