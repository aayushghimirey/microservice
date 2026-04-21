package com.sts.dto.response;

import java.math.BigDecimal;

public record PurchaseInfo(
        Long purchaseCount,
        BigDecimal purchaseAmount
) {
}
