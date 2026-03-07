package com.sts.service;

 import com.sts.event.MenuIngredientResponse;
 import com.sts.event.MenuResponse;
import com.sts.dto.CreateMenuRequest;
import com.sts.model.MenuIngredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface MenuService {


    MenuResponse createMenu(CreateMenuRequest request);

    Page<MenuResponse> getAllMenus(Pageable pageable);

    MenuResponse getMenuById(UUID menuId);

    List<MenuIngredientResponse> getMenuIngredentsById(UUID menuId);

}
