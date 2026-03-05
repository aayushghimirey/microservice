package com.sts.dto.request;


import java.math.BigDecimal;
import java.util.UUID;

public record StockAdjustmentCommand(
        UUID variantId,
        UUID unitId,
        BigDecimal quantity,
        String reason
) {

}
