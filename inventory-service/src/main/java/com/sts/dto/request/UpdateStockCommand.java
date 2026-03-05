package com.sts.dto.request;

import com.sts.constant.enums.StockType;
import com.sts.constant.enums.UnitType;

import java.math.BigDecimal;
import java.util.List;

public record UpdateStockCommand(
        String name,
        StockType type,
        List<VariantItemUpdate> variants
) {

    public record VariantItemUpdate(
            String name,
            String baseUnit,
            BigDecimal openingStock,
            List<VariantUnitUpdate> units
    ) {
    }

    public record VariantUnitUpdate(
            String name,
            BigDecimal conversionRate,
            UnitType unitType
    ) {
    }
}
