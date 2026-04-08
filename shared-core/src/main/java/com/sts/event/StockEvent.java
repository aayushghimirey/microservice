package com.sts.event;


import java.util.List;
import java.util.UUID;

public record StockEvent(
        UUID stockId,
        UUID tenantId,
        List<VariantStockEvent> variants
) {
    public record VariantStockEvent(
            UUID variantId,
            List<UUID> unitIds
    ) {
    }
}