package com.sts.client;

import com.sts.response.ApiResponse;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryGateway {
    private static final Logger log = LoggerFactory.getLogger(InventoryGateway.class);
    private final InventoryClient inventoryClient;


    public void validateOrThrow(UUID variantId, UUID unitId) {
        log.info(AppConstants.LOG_MESSAGES.CALLING_INVENTORY_SERVICE, variantId, unitId);
        ResponseEntity<ApiResponse<Boolean>> response = inventoryClient.existsVariantUnit(variantId, unitId);

        if (response == null || response.getBody() == null) {
            throw new ResourceNotFoundException("NOt found");
        }

        if (!response.getBody().getData()) {
            throw new ResourceNotFoundException("NOt found");
        }


    }


}
