package com.sts.purchase.command;

import com.sts.model.purchase.Purchase;
import com.sts.model.purchase.PurchaseItem;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class PurchaseCommandHandler {

    private final StockVariantRepository stockVariantRepository;
    private final VariantUnitRepository variantUnitRepository;

    public Purchase buildPurchase(CreatePurchaseCommand command) {
        if (command == null) return null;

        Purchase purchase = Purchase.builder()
                .invoiceNumber(command.invoiceNumber())
                .billingType(command.billingType())
                .moneyTransaction(command.moneyTransaction())
                .discountAmount(command.discountAmount() != null ? command.discountAmount() : BigDecimal.ZERO)
                .build();

        if (command.items() != null) {
            command.items().forEach(itemCmd -> {
                PurchaseItem item = buildPurchaseItem(itemCmd);
                purchase.addItem(item); // sets purchase in item
            });
        }

        // Calculate totals (subTotal, VAT, grossTotal)
        purchase.calculateTotal();

        return purchase;
    }

    private PurchaseItem buildPurchaseItem(CreatePurchaseCommand.PurchaseItemCommand itemCmd) {
        if (itemCmd == null) return null;

        if (!stockVariantRepository.existsById(itemCmd.variantId())) {
            throw new RuntimeException("Variant id not found");
        }
        if (!variantUnitRepository.existsById(itemCmd.unitId())) {
            throw new RuntimeException("Unit id not found");
        }

        return PurchaseItem.builder()
                .variantId(itemCmd.variantId())
                .unitId(itemCmd.unitId())
                .quantity(itemCmd.quantity() != null ? itemCmd.quantity() : BigDecimal.ZERO)
                .perUnitPrice(itemCmd.perUnitPrice() != null ? itemCmd.perUnitPrice() : BigDecimal.ZERO)
                .discountAmount(itemCmd.discountAmount() != null ? itemCmd.discountAmount() : BigDecimal.ZERO)
                .build();
    }


}
