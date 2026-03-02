package com.sts.purchase.application.mapper;


import com.sts.purchase.domain.model.Purchase;
import com.sts.purchase.domain.model.PurchaseItem;
import com.sts.purchase.dto.PurchaseResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PurchaseResponseMapper {

    public PurchaseResponse toResponse(Purchase purchase) {
        return new PurchaseResponse(
                purchase.getId(),
                purchase.getInvoiceNumber(),
                purchase.getBillingType(),
                purchase.getMoneyTransaction(),
                purchase.getDiscountAmount(),
                purchase.getSubTotal(),
                purchase.getVatAmount(),
                purchase.getGrossTotal(),
                purchase.getPurchaseItems().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList())
        );
    }

    private PurchaseResponse.PurchaseItemResponse toItemResponse(PurchaseItem item) {
        return new PurchaseResponse.PurchaseItemResponse(
                item.getVariantId(),
                item.getUnitId(),
                item.getQuantity(),
                item.getPerUnitPrice(),
                item.getDiscountAmount(),
                item.getSubTotal(),
                item.getNetTotal()
        );
    }
}