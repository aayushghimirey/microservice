package com.sts.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sts.event.MenuIngredientResponse;
import com.sts.response.ApiResponse;

@FeignClient(name = "menu-client", url = "${app.clients.menu-service.url}", path = "/menus")
public interface MenuClient {

    @GetMapping("/{menuId}/ingredients")
    ResponseEntity<ApiResponse<List<MenuIngredientResponse>>> getMenuIngredientsById(@PathVariable UUID menuId);

}
