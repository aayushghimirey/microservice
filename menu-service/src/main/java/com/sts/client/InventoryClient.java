package com.sts.client;

import com.sts.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "inventory-client",
        url = "${app.clients.inventory-service.url}",
        path = "/stocks"
)
public interface InventoryClient {

    @GetMapping("/variants/{variantId}/units/{unitId}/exists")
      ResponseEntity<ApiResponse<Boolean>> existsVariantUnit(   @PathVariable UUID variantId,
                                                                @PathVariable UUID unitId);
}