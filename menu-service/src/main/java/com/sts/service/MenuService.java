package com.sts.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sts.dto.CreateMenuRequest;
import com.sts.event.MenuIngredientResponse;
import com.sts.event.MenuResponse;

public interface MenuService {

    MenuResponse createMenu(CreateMenuRequest request);

    Page<MenuResponse> getAllMenus(Pageable pageable);

    MenuResponse getMenuById(UUID menuId);

    List<MenuIngredientResponse> getMenuIngredientsById(UUID menuId);

}
