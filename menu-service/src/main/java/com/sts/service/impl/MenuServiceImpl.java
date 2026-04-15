package com.sts.service.impl;

import com.sts.dto.CreateMenuRequest;
import com.sts.dto.MenuQueryDto;
import com.sts.event.MenuIngredientResponse;
import com.sts.event.MenuResponse;
import com.sts.exception.DuplicateResourceException;
import com.sts.exception.ResourceNotFoundException;
import com.sts.filter.TenantHolder;
import com.sts.mapper.MenuMapper;
import com.sts.model.Menu;
import com.sts.model.VariantSnapshot;
import com.sts.repository.MenuRepository;
import com.sts.repository.VariantSnapshotRepository;
import com.sts.service.MenuService;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.MenuCategory;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final VariantSnapshotRepository variantSnapshotRepository;
    private final MenuMapper menuMapper;

    private final RlsContext rlsContext;

    @Override
    @Transactional
    public MenuResponse createMenu(CreateMenuRequest request) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        if (menuRepository.existsByName(request.name())) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.DUPLICATE_MENU_NAME, request.name()));
        }


        if (request.code() != null && menuRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.DUPLICATE_MENU_CODE, request.code()));
        }

        validateIngredients(request);

        Menu menu = menuMapper.toEntity(request);
        menu = menuRepository.save(menu);
        return menuMapper.toResponse(menu);
    }

    // ── Queries ─────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<MenuResponse> getAllMenus(MenuQueryDto menuQueryDto, Pageable pageable) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Specification<Menu> spec = Specification
                .where((Specification<Menu>) (root, query, cb) -> menuQueryDto.category() == null ? null
                        : cb.equal(root.<MenuCategory>get("category"), menuQueryDto.category()))
                .and((Specification<Menu>) (root, query, cb) -> !StringUtils.hasText(menuQueryDto.menuName()) ? null
                        : cb.like(root.<String>get("menuName"), "%" + menuQueryDto.menuName() + "%"))
                .and((Specification<Menu>) (root, query, cb) -> !StringUtils.hasText(menuQueryDto.code()) ? null
                        : cb.equal(root.<String>get("code"), menuQueryDto.code()));

        return menuRepository.findAll(spec, pageable).map(menuMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuResponse getMenuById(UUID menuId) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Menu menu = findMenuOrThrow(menuId);
        return menuMapper.toResponse(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuIngredientResponse> getMenuIngredientsById(UUID menuId) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();


        Menu menu = findMenuOrThrow(menuId);
        return menu.getIngredients().stream()
                .map(menuMapper::toMenuIngredientResponse).toList();
    }

    // ── Private helpers ─────────────────────────────────────────────────────

    private Menu findMenuOrThrow(UUID menuId) {
        return menuRepository.findById(menuId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(AppConstants.ERROR_MESSAGES.MENU_NOT_FOUND, menuId)));
    }

    private void validateIngredients(CreateMenuRequest request) {
        if (request.ingredients() == null) return;
        request.ingredients().forEach(ingredient -> {
            VariantSnapshot variantSnapshot = variantSnapshotRepository.findByVariantId(ingredient.variantId()).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("Variant not found with id %s", ingredient.variantId())));

            variantSnapshot.getUnitIds().stream()
                    .filter(unit -> unit.equals(ingredient.unitId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Unit with id %s not found for variant %s", ingredient.unitId(), ingredient.variantId())));
        });
    }


}
