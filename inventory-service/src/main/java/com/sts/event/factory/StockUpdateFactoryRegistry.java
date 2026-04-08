package com.sts.event.factory;

import com.sts.event.InvoiceEvent;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.impl.AdjustmentEventFactory;
import com.sts.event.factory.impl.InvoiceEventFactory;
import com.sts.event.factory.impl.PurchaseEventFactory;
import com.sts.model.purchase.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;


/*
 * Domain event helper for stock update
 * */
@Component
@RequiredArgsConstructor
public class StockUpdateFactoryRegistry {

    private final AdjustmentEventFactory adjustmentEventFactory;
    private final InvoiceEventFactory invoiceEventFactory;
    private final PurchaseEventFactory purchaseEventFactory;


    public StockUpdateEvent forAdjustment(UUID variantId, UUID unitId, BigDecimal quantity, UUID tenantId) {
        return adjustmentEventFactory.build(new AdjustmentEventFactory.AdjustmentInput(variantId, unitId, tenantId, quantity));
    }


    public StockUpdateEvent forInvoice(InvoiceEvent event) {
        return invoiceEventFactory.build(event);
    }

    public StockUpdateEvent forPurchase(Purchase input) {
        return purchaseEventFactory.build(input);
    }

}
