package com.sts.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record StockUpdateEvent(
        UUID purchaseId,
        List<Info> event
) {

    public record Info(
            UUID variantId,
            UUID unitId,
            BigDecimal quantity,
            String reason
    ) {
    }

}
