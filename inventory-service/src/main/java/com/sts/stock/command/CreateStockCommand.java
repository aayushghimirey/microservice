package com.sts.stock.command;

import com.sts.stock.domain.enums.StockType;
import com.sts.stock.domain.enums.UnitType;

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