package com.sts.controller;

import com.sts.event.MenuResponse;
 import com.sts.utils.constant.AppConstants;
import com.sts.dto.CreateMenuRequest;
import com.sts.pagination.PageRequestDto;
import com.sts.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping(AppConstants.MENU_BASE_PATH)
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@Valid @RequestBody CreateMenuRequest request) {
        MenuResponse response = menuService.createMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<MenuResponse>> getAllMenus(@ModelAttribute PageRequestDto pageRequestDto) {
        Page<MenuResponse> menus = menuService.getAllMenus(pageRequestDto.buildPageable());
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponse> getMenuById(@PathVariable UUID menuId) {
        return ResponseEntity.ok(menuService.getMenuById(menuId));
    }
}
