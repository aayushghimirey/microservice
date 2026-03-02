package com.sts.infra.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "inventory-validator", url = "${spring.feign.inventory-service.url}", path = "/stock")
public interface FeignInventoryValidatorClient {

    @GetMapping("/{stockId}/{variantId}/validate")
    boolean validateStock(@PathVariable("stockId") UUID stockId, @PathVariable("variantId") UUID variantId);
}
