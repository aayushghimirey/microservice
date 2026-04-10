package com.sts.utils.feign;

import com.sts.event.MenuIngredientResponse;
import com.sts.event.MenuResponse;
import com.sts.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "menu-client", url = "${app.clients.menu-service.url}", path = "/menus", configuration = {FeignClientInterceptor.class})
public interface MenuClient {

    @GetMapping("/{menuId}")
    ResponseEntity<ApiResponse<MenuResponse>> getMenuById(@PathVariable UUID menuId);

}
