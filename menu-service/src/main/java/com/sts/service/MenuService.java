package com.sts.service;

import com.sts.event.MenuResponse;
import com.sts.dto.CreateMenuRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface MenuService {


    MenuResponse createMenu(CreateMenuRequest request);

    Page<MenuResponse> getAllMenus(Pageable pageable);

    MenuResponse getMenuById(UUID menuId);

}
