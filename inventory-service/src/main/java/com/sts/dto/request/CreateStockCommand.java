package com.sts.dto.request;

import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.UnitType;
import com.sts.utils.enums.StockType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CreateStockCommand(
        @NotBlank(message = AppConstants.VALIDATION_MESSAGES.STOCK_NAME_REQUIRED)
        String name,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.STOCK_TYPE_REQUIRED)
        StockType type,

        @NotEmpty(message = AppConstants.VALIDATION_MESSAGES.LEAST_ONE_VARIANT_REQUIRED)
        @Valid
        List<VariantItemCommand> variants
) {
    @Builder

    public record VariantItemCommand(

            @NotBlank(message = AppConstants.VALIDATION_MESSAGES.VARIANT_NAME_REQUIRED)
            String name,

            @NotBlank(message = AppConstants.VALIDATION_MESSAGES.BASE_UNIT_REQUIRED)
            String baseUnit,

            BigDecimal openingStock,

            @NotEmpty(message = AppConstants.VALIDATION_MESSAGES.LEAST_ONE_VARIANT_REQUIRED)
            @Valid
            List<VariantUnitCommand> units

    ) {
    }

    @Builder

    public record VariantUnitCommand(

            @NotBlank(message = AppConstants.VALIDATION_MESSAGES.UNIT_NAME_REQUIRED)
            String name,

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.CONVERSION_RATE_REQUIRED)
            @Positive(message = AppConstants.VALIDATION_MESSAGES.CONVERSION_RATE_POSITIVE)
            BigDecimal conversionRate,

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.UNIT_TYPE_REQUIRED)
            UnitType unitType

    ) {
    }

}