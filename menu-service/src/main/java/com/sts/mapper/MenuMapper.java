package com.sts.mapper;

import com.sts.dto.CreateMenuRequest;
import com.sts.event.MenuIngredientResponse;
import com.sts.event.MenuResponse;
import com.sts.model.Menu;
import com.sts.model.MenuIngredient;
import org.springframework.stereotype.Component;


@Component
public final class MenuMapper {


    public Menu toEntity(CreateMenuRequest request) {
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


    public MenuResponse toResponse(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getCode(),
                menu.getCategory().name(),
                menu.getPrice(),
                menu.getIngredients().stream()
                        .map(this::toMenuIngredientResponse)
                        .toList()
        );

    }

    public MenuIngredientResponse toMenuIngredientResponse(MenuIngredient menuIngredient) {
        return new MenuIngredientResponse(
                menuIngredient.getVariantId(),
                menuIngredient.getUnitId(),
                menuIngredient.getQuantity()
        );
    }

    // ── Private helpers ────────────────────────────────────────────────────

    private MenuIngredient toIngredientEntity(CreateMenuRequest.MenuIngredientRequest req) {
        MenuIngredient ingredient = new MenuIngredient();
        ingredient.setVariantId(req.variantId());
        ingredient.setUnitId(req.unitId());
        ingredient.setQuantity(req.quantity());
        return ingredient;
    }

}
