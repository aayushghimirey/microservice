package com.sts.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record StockEvent(
        UUID id,
        String name,
        String type,
        List<VariantStockEvent> variants
) {

    public record VariantStockEvent(
            UUID id,
            String name,
            String baseUnit,
            BigDecimal openingStock,
            BigDecimal currentStock,
            List<UnitStockEvent> units
    ) {
    }

    public record UnitStockEvent(
            UUID id,
            String name,
            BigDecimal conversionRate,
            String unitType
    ) {
    }
}