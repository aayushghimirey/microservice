package com.sts.mapper;

import com.sts.dto.CreateMenuRequest;
import com.sts.event.MenuResponse;
import com.sts.model.Menu;
import com.sts.model.MenuIngredient;

import java.util.List;


public final class MenuMapper {

    private MenuMapper() {
    }


    public static Menu toEntity(CreateMenuRequest request) {
        Menu menu = new Menu();
        menu.setName(request.name());
        menu.setCode(request.code());
        menu.setCategory(request.category());
        menu.setPrice(request.price());

        if (request.ingredients() != null) {
            request.ingredients().forEach(ingredientReq -> {
                MenuIngredient ingredient = toIngredientEntity(ingredientReq);
                menu.addIngredient(ingredient);
            });
        }
        return menu;
    }


    public static MenuResponse toResponse(Menu menu) {


        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getCode(),
                menu.getCategory().name(),
                menu.getPrice()
        );
    }

    // ── Private helpers ────────────────────────────────────────────────────

    private static MenuIngredient toIngredientEntity(CreateMenuRequest.MenuIngredientRequest req) {
        MenuIngredient ingredient = new MenuIngredient();
        ingredient.setVariantId(req.variantId());
        ingredient.setUnitId(req.unitId());
        ingredient.setQuantity(req.quantity());
        return ingredient;
    }

}
