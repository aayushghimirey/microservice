package com.sts.mapper;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.sts.dto.request.UpdateStockCommand;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;

@Component
public final class StockUpdateMapper {

    public void updateStock(Stock stock, UpdateStockCommand command) {
        if (command.name() != null)
            stock.setName(command.name());
        if (command.type() != null)
            stock.setType(command.type());

        if (command.variants() != null) {
            for (var variantCmd : command.variants()) {
                // check for exiting variants
                // filter by name if, update variant name exits then apply updated ,
                // else build new variant
                Optional<StockVariant> existingVariant = stock.getVariants().stream()
                        .filter(v -> v.getName().equals(variantCmd.name()))
                        .findFirst();

                StockVariant variant;
                if (existingVariant.isPresent()) {
                    variant = existingVariant.get();
                    updateVariant(variant, variantCmd);
                } else {
                    // new variant
                    variant = buildVariant(variantCmd);
                    stock.addVariant(variant);
                }
            }
        }
    }

    // stock adjustment logic removed, relies on events directly

    private void updateVariant(StockVariant variant, UpdateStockCommand.VariantItemUpdate cmd) {
        if (cmd.baseUnit() != null)
            variant.setBaseUnit(cmd.baseUnit());
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
                    if (unitCmd.conversionRate() != null)
                        unit.setConversionRate(unitCmd.conversionRate());
                    if (unitCmd.unitType() != null)
                        unit.setUnitType(unitCmd.unitType());
                } else {
                    VariantUnit newUnit = buildUnit(unitCmd);
                    variant.addUnit(newUnit);
                }
            }
        }
    }

    private StockVariant buildVariant(UpdateStockCommand.VariantItemUpdate cmd) {
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

    private VariantUnit buildUnit(UpdateStockCommand.VariantUnitUpdate cmd) {
        return VariantUnit.builder()
                .name(cmd.name())
                .conversionRate(cmd.conversionRate() != null ? cmd.conversionRate() : BigDecimal.ONE)
                .unitType(cmd.unitType())
                .build();
    }
}