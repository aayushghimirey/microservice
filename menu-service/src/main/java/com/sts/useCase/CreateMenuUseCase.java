package com.sts.useCase;

import com.sts.command.CreateMenuCommand;
import com.sts.command.MenuCommandHandler;
import com.sts.command.MenuIngredientHandler;
import com.sts.domain.model.Menu;
import com.sts.domain.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateMenuUseCase {

    private final MenuRepository menuRepository;
    private final MenuCommandHandler menuCommandHandler;
    private final MenuIngredientHandler menuIngredientHandler;

    @Transactional
    public Menu createMenu(CreateMenuCommand command) {

        // validate ingredients
        command.ingredients().forEach(item -> menuIngredientHandler.validateIngredients(item.variantId(), item.unitId()));

        Menu menu = menuCommandHandler.buildMenu(command);

        return menuRepository.save(menu);
    }

}
