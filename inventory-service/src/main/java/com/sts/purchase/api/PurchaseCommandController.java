package com.sts.purchase.api;

import com.sts.purchase.command.CreatePurchaseCommand;
import com.sts.purchase.domain.model.Purchase;
import com.sts.purchase.dto.PurchaseResponse;
import com.sts.purchase.application.mapper.PurchaseResponseMapper;
import com.sts.purchase.application.usecase.PurchaseCommandUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
@AllArgsConstructor
public class PurchaseCommandController {

    private final PurchaseCommandUseCase purchaseCommandUseCase;
    private final PurchaseResponseMapper purchaseResponseMapper;

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@Valid @RequestBody CreatePurchaseCommand command) {
        Purchase purchase = purchaseCommandUseCase.createPurchase(command);
        return ResponseEntity.ok().body(purchaseResponseMapper.toResponse(purchase));
    }

}
