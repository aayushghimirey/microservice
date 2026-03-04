package com.sts.service;

import com.sts.event.MenuResponse;
import com.sts.utils.constant.AppConstants;
import com.sts.dto.CreateMenuRequest;
import com.sts.exception.IngredientValidationException;
import com.sts.exception.MenuNotFoundException;
import com.sts.utils.feign.InventoryClient;
import com.sts.mapper.MenuMapper;
import com.sts.model.Menu;
import com.sts.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final InventoryClient inventoryClient;

    // ── Commands ────────────────────────────────────────────────────────────

    @Transactional
    public MenuResponse createMenu(CreateMenuRequest request) {
        log.info("Creating menu with category: {}", request.category());

        if(request.code() != null) {
            if (menuRepository.existsByCode(request.code())) {
                throw new IllegalArgumentException(
                        String.format(AppConstants.DUPLICATE_MENU_CODE, request.code()));
            }
        }

        validateIngredients(request);

        Menu menu = MenuMapper.toEntity(request);
        Menu saved = menuRepository.save(menu);

        log.info("Menu created successfully with id: {}", saved.getId());
        return MenuMapper.toResponse(saved);
    }

    // ── Queries ─────────────────────────────────────────────────────────────


    @Transactional(readOnly = true)
    public Page<MenuResponse> getAllMenus(Pageable pageable) {
        log.debug("Fetching all menus — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return menuRepository.findAll(pageable)
                .map(MenuMapper::toResponse);
    }


    @Transactional(readOnly = true)
    public MenuResponse getMenuById(UUID menuId) {
        log.debug("Fetching menu with id: {}", menuId);
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException(AppConstants.MENU_NOT_FOUND + menuId));
        return MenuMapper.toResponse(menu);
    }

    // ── Private helpers ─────────────────────────────────────────────────────

    private void validateIngredients(CreateMenuRequest request) {
        if (request.ingredients() == null)
            return;
        request.ingredients().forEach(ingredient -> {
            boolean valid = inventoryClient.validateStock(ingredient.variantId(), ingredient.unitId());
            if (!valid) {
                throw new IngredientValidationException(
                        String.format(AppConstants.INVALID_INGREDIENT,
                                ingredient.variantId(), ingredient.unitId()));
            }
        });
    }
}
