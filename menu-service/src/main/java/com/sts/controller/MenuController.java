package com.sts.controller;

 import com.sts.event.MenuIngredientResponse;
 import com.sts.event.MenuResponse;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.utils.constant.AppConstants;
import com.sts.dto.CreateMenuRequest;
import com.sts.pagination.PageRequestDto;
import com.sts.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(AppConstants.MENU_BASE_PATH)
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(@Valid @RequestBody CreateMenuRequest request) {
        var menu = menuService.createMenu(request);
        return AppResponse.success(menu, "Menu created successfully");
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<MenuResponse>>> getAllMenus(@ModelAttribute PageRequestDto pageRequestDto) {
        var menus = menuService.getAllMenus(pageRequestDto.buildPageable());
        return AppResponse.success(menus, "Menus fetched successfully");
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenuById(@PathVariable UUID menuId) {
        var menuById = menuService.getMenuById(menuId);
        return AppResponse.success(menuById, "Menu fetched successfully");
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<List<MenuIngredientResponse>>> getMenuIngredentsById(@PathVariable UUID menuId) {
        var menuById = menuService.getMenuIngredentsById(menuId);
        return AppResponse.success(menuById, "Menu fetched successfully");
    }
}
