package com.sts.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.CreateMenuRequest;
import com.sts.event.MenuIngredientResponse;
import com.sts.event.MenuResponse;
import com.sts.exception.BusinessValidationException;
import com.sts.exception.DuplicateResourceException;
import com.sts.exception.ResourceNotFoundException;
import com.sts.mapper.MenuMapper;
import com.sts.model.Menu;
import com.sts.repository.MenuRepository;
import com.sts.response.ApiResponse;
import com.sts.service.MenuService;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.feign.InventoryClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final InventoryClient inventoryClient;
    private final MenuMapper menuMapper;

    // ── Commands ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public MenuResponse createMenu(CreateMenuRequest request) {
        log.debug(AppConstants.LOG_MESSAGES.CREATING_MENU, request.category());


        if (menuRepository.existsByName(request.name())) {
            throw new DuplicateResourceException(String.format(AppConstants.ERROR_MESSAGES.DUPLICATE_MENU_NAME, request.name()));
        }


        if (request.code() != null && menuRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException(String.format(AppConstants.ERROR_MESSAGES.DUPLICATE_MENU_CODE, request.code()));
        }

        validateIngredients(request);

        Menu menu = menuMapper.toEntity(request);
        Menu saved = menuRepository.save(menu);

        log.debug(AppConstants.LOG_MESSAGES.MENU_CREATED, saved.getId());
        return menuMapper.toResponse(saved);
    }

    // ── Queries ─────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<MenuResponse> getAllMenus(Pageable pageable) {
        log.debug(AppConstants.LOG_MESSAGES.FETCHING_ALL_MENUS, pageable.getPageNumber(), pageable.getPageSize());
        return menuRepository.findAll(pageable).map(menuMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuResponse getMenuById(UUID menuId) {
        log.debug(AppConstants.LOG_MESSAGES.FETCHING_MENU_BY_ID, menuId);
        Menu menu = findMenuOrThrow(menuId);
        return menuMapper.toResponse(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuIngredientResponse> getMenuIngredientsById(UUID menuId) {
        log.debug(AppConstants.LOG_MESSAGES.FETCHING_MENU_INGREDIENTS, menuId);
        Menu menu = findMenuOrThrow(menuId);
        return menu.getIngredients().stream().map(menuMapper::toMenuIngredientResponse).toList();
    }

    // ── Private helpers ─────────────────────────────────────────────────────

    private Menu findMenuOrThrow(UUID menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.ERROR_MESSAGES.MENU_NOT_FOUND, menuId)));
    }

    private void validateIngredients(CreateMenuRequest request) {
        if (request.ingredients() == null) return;
        request.ingredients().forEach(ingredient -> {
            var response = inventoryClient.validateStock(ingredient.variantId(), ingredient.unitId());
            ApiResponse<Boolean> body = response.getBody();

            if (body == null || body.getData() == null) {
                throw new BusinessValidationException(String.format(AppConstants.ERROR_MESSAGES.INVALID_STOCK_RESPONSE, ingredient.variantId(), ingredient.unitId()));
            }

            if (!body.getData()) {
                throw new BusinessValidationException(String.format(AppConstants.ERROR_MESSAGES.INVALID_INGREDIENT, ingredient.variantId(), ingredient.unitId()));
            }
        });
    }

}
