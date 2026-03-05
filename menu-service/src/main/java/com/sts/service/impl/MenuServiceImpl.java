package com.sts.service.impl;

import com.sts.dto.CreateMenuRequest;
import com.sts.event.MenuResponse;
import com.sts.exception.IngredientValidationException;
import com.sts.exception.MenuNotFoundException;
import com.sts.mapper.MenuMapper;
import com.sts.model.Menu;
import com.sts.repository.MenuRepository;
import com.sts.service.MenuService;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.feign.InventoryClient;
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
class MenuServiceImpl implements MenuService {


    private final MenuRepository menuRepository;
    private final InventoryClient inventoryClient;
    private final MenuMapper menuMapper;

    // ── Commands ────────────────────────────────────────────────────────────

    @Transactional
    public MenuResponse createMenu(CreateMenuRequest request) {
        log.info("Creating menu with category: {}", request.category());

        if (request.code() != null) {
            if (menuRepository.existsByCode(request.code())) {
                throw new IllegalArgumentException(
                        String.format(AppConstants.DUPLICATE_MENU_CODE, request.code()));
            }
        }

        if (menuRepository.findByName(request.name())) {
            throw new IllegalArgumentException(
                    String.format(AppConstants.DUPLICATE_MENU_NAME, request.code()));
        }

        validateIngredients(request);

        Menu menu = menuMapper.toEntity(request);
        Menu saved = menuRepository.save(menu);

        log.info("Menu created successfully with id: {}", saved.getId());
        return menuMapper.toResponse(saved);
    }

    // ── Queries ─────────────────────────────────────────────────────────────


    @Transactional(readOnly = true)
    public Page<MenuResponse> getAllMenus(Pageable pageable) {
        log.debug("Fetching all menus — page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return menuRepository.findAll(pageable)
                .map(menuMapper::toResponse);
    }


    @Transactional(readOnly = true)
    public MenuResponse getMenuById(UUID menuId) {
        log.debug("Fetching menu with id: {}", menuId);
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException(AppConstants.MENU_NOT_FOUND + menuId));
        return menuMapper.toResponse(menu);
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
