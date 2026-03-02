package com.sts.command;

import com.sts.domain.enums.MenuCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateMenuCommand(
        String name,
        String code,
        MenuCategory category,
        BigDecimal price,

        List<MenuIngredientCommand> ingredients
) {

    public record MenuIngredientCommand(
            UUID variantId,
            UUID unitId,
            double quantity
    ) {
    }

}
