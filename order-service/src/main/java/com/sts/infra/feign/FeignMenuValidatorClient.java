package com.sts.infra.feign;

import com.sts.event.MenuResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "menu-validator", url = "${spring.feign.inventory-service.url}", path = "/menu")
public interface FeignMenuValidatorClient {

    @GetMapping("/{menuId}")
    ResponseEntity<MenuResponseDto> getMenuById(@PathVariable("menuId") UUID menuId);

}
