package com.sts.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.MenuCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateMenuRequest(

        @NotBlank(message = AppConstants.VALIDATION_MESSAGES.MENU_NAME_REQUIRED) String name,

        String code,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.MENU_CATEGORY_REQUIRED) MenuCategory category,

        @NotNull(message = AppConstants.VALIDATION_MESSAGES.PRICE_REQUIRED) @Positive(message = AppConstants.VALIDATION_MESSAGES.PRICE_POSITIVE) BigDecimal price,

        @NotEmpty(message = AppConstants.VALIDATION_MESSAGES.INGREDIENTS_REQUIRED) @Valid List<MenuIngredientRequest> ingredients

) {

    /**
     * Nested record representing a single ingredient entry on a menu.
     */
    public record MenuIngredientRequest(

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.VARIANT_ID_REQUIRED) UUID variantId,

            @NotNull(message = AppConstants.VALIDATION_MESSAGES.UNIT_ID_REQUIRED) UUID unitId,

            @Positive(message = AppConstants.VALIDATION_MESSAGES.QUANTITY_POSITIVE) double quantity) {
    }
}
