package com.sts.mapper;

import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

public final class StockUpdateMapper {

    private StockUpdateMapper(){}

    public static void updateStock(Stock stock, UpdateStockCommand command) {
        if (command.name() != null) stock.setName(command.name());
        if (command.type() != null) stock.setType(command.type());

        if (command.variants() != null) {
            for (var variantCmd : command.variants()) {
                Optional<StockVariant> existingVariant = stock.getVariants().stream()
                        .filter(v -> v.getName().equals(variantCmd.name()))
                        .findFirst();

                StockVariant variant;
                if (existingVariant.isPresent()) {
                    variant = existingVariant.get();
                    updateVariant(variant, variantCmd);
                } else {
                    variant = buildVariant(variantCmd);
                    stock.addVariant(variant);
                }
            }
        }
    }

    // stock adjustment
    public static void adjustStock(StockVariant variant, VariantUnit variantUnit, StockAdjustmentCommand command) {
        BigDecimal currentStock = variant.getCurrentStock();
        BigDecimal adjustmentQuantity = command.quantity().multiply(variantUnit.getConversionRate());

        if (adjustmentQuantity.compareTo(BigDecimal.ZERO) > 0) {
            variant.setCurrentStock(currentStock.subtract(adjustmentQuantity));
        }


    }

    private static void updateVariant(StockVariant variant, UpdateStockCommand.VariantItemUpdate cmd) {
        if (cmd.baseUnit() != null) variant.setBaseUnit(cmd.baseUnit());
        if (cmd.openingStock() != null && cmd.openingStock().compareTo(BigDecimal.ZERO) > 0) {
            variant.setOpeningStock(cmd.openingStock());
            if (variant.getCurrentStock() == null || variant.getCurrentStock().compareTo(BigDecimal.ZERO) <= 0) {
                variant.setCurrentStock(cmd.openingStock());
            }
        }

        if (cmd.units() != null) {
            for (var unitCmd : cmd.units()) {
                Optional<VariantUnit> existingUnit = variant.getUnits().stream()
                        .filter(u -> u.getName().equals(unitCmd.name()))
                        .findFirst();

                if (existingUnit.isPresent()) {
                    VariantUnit unit = existingUnit.get();
                    if (unitCmd.conversionRate() != null) unit.setConversionRate(unitCmd.conversionRate());
                    if (unitCmd.unitType() != null) unit.setUnitType(unitCmd.unitType());
                } else {
                    VariantUnit newUnit = buildUnit(unitCmd);
                    variant.addUnit(newUnit);
                }
            }
        }
    }

    private static StockVariant buildVariant(UpdateStockCommand.VariantItemUpdate cmd) {
        StockVariant variant = StockVariant.builder()
                .name(cmd.name())
                .baseUnit(cmd.baseUnit())
                .openingStock(cmd.openingStock() != null ? cmd.openingStock() : BigDecimal.ZERO)
                .currentStock(BigDecimal.ZERO)
                .build();

        if (cmd.units() != null) {
            for (var unitCmd : cmd.units()) {
                variant.addUnit(buildUnit(unitCmd));
            }
        }
        return variant;
    }

    private static VariantUnit buildUnit(UpdateStockCommand.VariantUnitUpdate cmd) {
        return VariantUnit.builder()
                .name(cmd.name())
                .conversionRate(cmd.conversionRate() != null ? cmd.conversionRate() : BigDecimal.ONE)
                .unitType(cmd.unitType())
                .build();
    }
}