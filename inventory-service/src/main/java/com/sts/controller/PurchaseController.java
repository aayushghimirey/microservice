package com.sts.controller;


import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.purchase.PurchaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseResponse>> createPurchase(
            @Valid @RequestBody CreatePurchaseCommand command
    ) {
        var purchase = purchaseService.createPurchase(command);
        return AppResponse.success(purchase, "Purchase created successfully");
    }

}
