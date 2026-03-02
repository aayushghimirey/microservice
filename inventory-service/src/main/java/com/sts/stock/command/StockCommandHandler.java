package com.sts.stock.command;

import com.sts.stock.domain.model.Stock;
import com.sts.stock.domain.model.StockVariant;
import com.sts.stock.domain.model.VariantUnit;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class StockCommandHandler {

    public Stock buildStock(CreateStockCommand command) {
        Stock stock = Stock.builder()
                .name(command.name())
                .type(command.type())
                .build();

        for (var variantCmd : command.variants()) {
            stock.addVariant(buildVariant(variantCmd));
        }

        return stock;
    }

    private StockVariant buildVariant(CreateStockCommand.VariantItemCommand variant) {
        StockVariant sv = StockVariant.builder()
                .name(variant.name())
                .baseUnit(variant.baseUnit())
                .build();

        sv.setOpeningStock(variant.openingStock() != null ? variant.openingStock() : BigDecimal.ZERO);

        for (var unitCmd : variant.units()) {
            sv.addUnit(buildUnit(unitCmd));
        }

        return sv;
    }

    private VariantUnit buildUnit(CreateStockCommand.VariantUnitCommand unit) {
        return VariantUnit.builder()
                .name(unit.name())
                .unitType(unit.unitType())
                .conversionRate(unit.conversionRate() != null ? unit.conversionRate() : BigDecimal.ONE)
                .build();
    }
}