package com.sts.client;

import com.sts.response.ApiResponse;
import lombok.RequiredArgsConstructor;
 import org.apache.kafka.common.errors.ResourceNotFoundException;
 import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryGateway {
    private final InventoryClient inventoryClient;


  public void  validateOrThrow(UUID variantId, UUID unitId){
      ResponseEntity<ApiResponse<Boolean>> response = inventoryClient.existsVariantUnit(variantId, unitId);

      if(response == null || response.getBody() == null){
          throw new ResourceNotFoundException("NOt found");
      }

      if(!response.getBody().getData()){
          throw new ResourceNotFoundException("NOt found");
      }



  }


}
