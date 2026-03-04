package com.sts.dto.command;

import com.sts.constant.enums.StockType;
import com.sts.constant.enums.UnitType;

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