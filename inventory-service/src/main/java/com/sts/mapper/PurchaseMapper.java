package com.sts.mapper;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import com.sts.model.purchase.Purchase;
import com.sts.model.purchase.PurchaseItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PurchaseMapper {


    public Purchase buildPurchase(CreatePurchaseCommand command) {
        if (command == null) return null;

        Purchase purchase = Purchase.builder()
                .invoiceNumber(command.invoiceNumber())
                .billingType(command.billingType())
                .moneyTransaction(command.moneyTransaction())
                .discountAmount(command.discountAmount() != null ? command.discountAmount() : BigDecimal.ZERO)
                .build();

        // mapping purchased items
        if (command.items() != null) {
            command.items().forEach(itemCmd -> {
                PurchaseItem item = buildPurchaseItem(itemCmd);
                purchase.addItem(item);
            });
        }

        // Calculate totals (subTotal, VAT, grossTotal)
        purchase.calculateTotal();

        return purchase;
    }

    private PurchaseItem buildPurchaseItem(CreatePurchaseCommand.PurchaseItemCommand itemCmd) {
        if (itemCmd == null) return null;

        return PurchaseItem.builder()
                .variantId(itemCmd.variantId())
                .unitId(itemCmd.unitId())
                .quantity(itemCmd.quantity() != null ? itemCmd.quantity() : BigDecimal.ZERO)
                .perUnitPrice(itemCmd.perUnitPrice() != null ? itemCmd.perUnitPrice() : BigDecimal.ZERO)
                .discountAmount(itemCmd.discountAmount() != null ? itemCmd.discountAmount() : BigDecimal.ZERO)
                .build();
    }


    // -- response mapper -----------------------------
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
