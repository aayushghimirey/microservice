package com.sts.dto.request;


import com.sts.utils.constant.AppConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record StockAdjustmentCommand(

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.VARIANT_ID_REQUIRED)
        UUID variantId,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.UNIT_ID_REQUIRED)
        UUID unitId,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.QUANTITY_REQUIRED)
        @Positive(message = AppConstants.VALIDATION_MESSAGES.QUANTITY_MUST_BE_POSITIVE)
        BigDecimal quantity,

        @Size(max = 255, message = AppConstants.VALIDATION_MESSAGES.REASON_TOO_LONG)
        String reason
) {
}