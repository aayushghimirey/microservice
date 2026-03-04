package com.sts.utils.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "inventory-client", url = "${app.clients.inventory-service.url}", path = "/stock")
public interface InventoryClient {

    @GetMapping("/{variantId}/{unitId}/validate")
    boolean validateStock(
            @PathVariable("variantId") UUID variantId,
            @PathVariable("unitId") UUID unitId);
}
