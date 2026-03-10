// =============================================================================
// DEPRECATED — Duplicate of com.sts.event.factory.PurchaseEventFactory
// The @Component annotation has been removed to prevent Spring bean conflict.
// The authoritative class lives in: com.sts.event.factory.PurchaseEventFactory
// =============================================================================

package com.sts.mapper;

import com.sts.event.PurchaseCreatedEvent;
import com.sts.model.purchase.Purchase;
// import org.springframework.stereotype.Component; // REMOVED — duplicate bean, see com.sts.event.factory.PurchaseEventFactory

// @Component  // COMMENTED OUT — duplicate; use com.sts.event.factory.PurchaseEventFactory
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