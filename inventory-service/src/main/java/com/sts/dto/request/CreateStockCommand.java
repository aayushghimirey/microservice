package com.sts.dto.request;

import com.sts.utils.enums.UnitType;
import com.sts.utils.enums.StockType;

import java.math.BigDecimal;
import java.util.List;

public record CreateStockCommand(
        String name,
        StockType type,
        List<VariantItemCommand> variants
) {

    public record VariantItemCommand(
            String name,
            String baseUnit,
            BigDecimal openingStock,
            List<VariantUnitCommand> units
    ) {
    }

    public record VariantUnitCommand(
            String name,
            BigDecimal conversionRate,
            UnitType unitType
    ) {
    }
}