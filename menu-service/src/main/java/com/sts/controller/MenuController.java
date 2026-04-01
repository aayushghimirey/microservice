package com.sts.controller;

import java.util.List;
import java.util.UUID;

import com.sts.dto.MenuQueryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.CreateMenuRequest;
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

@RestController
@RequestMapping(AppConstants.MENU_BASE_PATH)
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(@Valid @RequestBody CreateMenuRequest request) {
        log.info(AppConstants.LOG_MESSAGES.CREATING_MENU, request.category());
        var menu = menuService.createMenu(request);
        return AppResponse.success(menu, AppConstants.SUCCESS_MESSAGES.MENU_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<MenuResponse>>> getAllMenus(@ModelAttribute MenuQueryDto menuQueryDto,
                                                                         @ModelAttribute PageRequestDto pageRequestDto) {
        log.info(AppConstants.LOG_MESSAGES.FETCHING_ALL_MENUS, pageRequestDto.getPage(), pageRequestDto.getSize());
        var menus = menuService.getAllMenus(menuQueryDto, pageRequestDto.buildPageable());
        return AppResponse.success(menus, AppConstants.SUCCESS_MESSAGES.MENUS_FETCHED);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenuById(@PathVariable UUID menuId) {
        var menuById = menuService.getMenuById(menuId);
        return AppResponse.success(menuById, AppConstants.SUCCESS_MESSAGES.MENU_FETCHED);
    }

    @GetMapping("/{menuId}/ingredients")
    public ResponseEntity<ApiResponse<List<MenuIngredientResponse>>> getMenuIngredientsById(@PathVariable UUID menuId) {
        log.info(AppConstants.LOG_MESSAGES.FETCHING_MENU_INGREDIENTS, menuId);
        var menuIngredients = menuService.getMenuIngredientsById(menuId);
        return AppResponse.success(menuIngredients, AppConstants.SUCCESS_MESSAGES.MENU_INGREDIENTS_FETCHED);
    }
}
