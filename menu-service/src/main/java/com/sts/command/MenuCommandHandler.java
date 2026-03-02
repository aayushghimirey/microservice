package com.sts.command;

import com.sts.domain.model.Menu;
import com.sts.domain.model.MenuIngredient;
import org.springframework.stereotype.Component;

@Component
public class MenuCommandHandler {

    public Menu buildMenu(CreateMenuCommand command) {
        Menu menu = new Menu();
        menu.setName(command.name());
        menu.setPrice(command.price());
        menu.setCode(command.code());
        menu.setCategory(command.category());

        for (CreateMenuCommand.MenuIngredientCommand ingredientCommand : command.ingredients()) {
            MenuIngredient ingredient = buildMenuIngredient(ingredientCommand);
            menu.addIngredient(ingredient);
        }
        return menu;
    }

    private MenuIngredient buildMenuIngredient(CreateMenuCommand.MenuIngredientCommand command) {
        MenuIngredient ingredient = new MenuIngredient();
        ingredient.setVariantId(command.variantId());
        ingredient.setUnitId(command.unitId());
        ingredient.setQuantity(command.quantity());
        return ingredient;
    }
}
