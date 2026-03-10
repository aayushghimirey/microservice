package com.sts.event;

import org.springframework.stereotype.Component;

import com.sts.model.purchase.Purchase;

@Component
public class PurchaseEventFactory {

    public PurchaseCreatedEvent buildPurchaseCreatedEvent(Purchase purchase) {
        return new PurchaseCreatedEvent(
                purchase.getId(),
                purchase.getBillingType(),
                purchase.getMoneyTransaction(),
                purchase.getVatAmount(),
                purchase.getGrossTotal());
    }

}
