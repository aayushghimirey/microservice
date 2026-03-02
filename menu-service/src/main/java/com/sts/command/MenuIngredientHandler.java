package com.sts.command;

import com.sts.infra.feign.FeignInventoryValidatorClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MenuIngredientHandler {

    private final FeignInventoryValidatorClient inventoryValidatorClient;

    public void validateIngredients(UUID variantId, UUID unitId) {
        if (!inventoryValidatorClient.validateStock(variantId, unitId)) {
            throw new IllegalStateException("Invalid variant or unit : " + variantId + " - " + unitId);
        }
    }

}
