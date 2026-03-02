package com.sts.stock.dto;

import com.sts.stock.domain.enums.StockType;
import com.sts.stock.domain.enums.UnitType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record StockResponse(
        UUID id,
        String name,
        StockType type,
        List<VariantResponse> variants
) {

    public record VariantResponse(
            UUID id,
            String name,
            String baseUnit,
            BigDecimal openingStock,
            BigDecimal currentStock,
            List<UnitResponse> units
    ) {
    }

    public record UnitResponse(
            UUID id,
            String name,
            BigDecimal conversionRate,
            UnitType unitType
    ) {
    }
}