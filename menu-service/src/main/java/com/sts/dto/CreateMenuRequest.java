package com.sts.dto;

import com.sts.utils.enums.MenuCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record   CreateMenuRequest(

        @NotBlank(message = "Menu name is required") String name,

         String code,

        @NotNull(message = "Menu category is required") MenuCategory category,

        @NotNull(message = "Price is required") @Positive(message = "Price must be a positive value") BigDecimal price,

        @NotEmpty(message = "At least one ingredient is required") @Valid List<MenuIngredientRequest> ingredients

) {

    /**
     * Nested record representing a single ingredient entry on a menu.
     */
    public record MenuIngredientRequest(

            @NotNull(message = "Variant ID is required") UUID variantId,

            @NotNull(message = "Unit ID is required") UUID unitId,

            @Positive(message = "Quantity must be a positive value") double quantity) {
    }
}
