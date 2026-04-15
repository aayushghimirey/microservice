package com.sts.controller;

import com.sts.dto.CreateMenuRequest;
import com.sts.dto.MenuQueryDto;
import com.sts.event.MenuIngredientResponse;
import com.sts.event.MenuResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.MenuService;
import com.sts.utils.constant.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(AppConstants.MENU_BASE_PATH)
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @RequestBody CreateMenuRequest request) {

        log.info("Request received: createMenu name={} category={}",
                request.name(), request.category());

        MenuResponse menu = menuService.createMenu(request);

        log.info("Request completed: createMenu menuId={}", menu.getId());

        return AppResponse.success(menu, AppConstants.SUCCESS_MESSAGES.MENU_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<MenuResponse>>> getAllMenus(
            @ModelAttribute MenuQueryDto menuQueryDto,
            @ModelAttribute PageRequestDto pageRequestDto) {

        log.info("Request received: getAllMenus page={} size={}",
                pageRequestDto.getPage(), pageRequestDto.getSize());

        var menus =
                menuService.getAllMenus(menuQueryDto, pageRequestDto.buildPageable());

        log.info("Request completed: getAllMenus totalElements={}",
                menus.getTotalElements());

        return AppResponse.success(menus, AppConstants.SUCCESS_MESSAGES.MENUS_FETCHED);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenuById(
            @PathVariable UUID menuId) {

        log.info("Request received: getMenuById menuId={}", menuId);

        MenuResponse menu = menuService.getMenuById(menuId);

        log.info("Request completed: getMenuById menuId={}", menu.getId());

        return AppResponse.success(menu, AppConstants.SUCCESS_MESSAGES.MENU_FETCHED);
    }

    @GetMapping("/{menuId}/ingredients")
    public ResponseEntity<ApiResponse<List<MenuIngredientResponse>>> getMenuIngredientsById(
            @PathVariable UUID menuId) {

        log.info("Request received: getMenuIngredientsById menuId={}", menuId);

        List<MenuIngredientResponse> ingredients =
                menuService.getMenuIngredientsById(menuId);

        log.info("Request completed: getMenuIngredientsById menuId={} count={}",
                menuId, ingredients.size());

        return AppResponse.success(ingredients, AppConstants.SUCCESS_MESSAGES.MENU_INGREDIENTS_FETCHED);
    }
}
