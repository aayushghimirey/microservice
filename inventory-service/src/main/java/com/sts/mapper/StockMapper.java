package com.sts.mapper;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class StockMapper {


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


    public StockResponse toResponse(Stock stock) {
        if (stock == null)
            return null;

        return new StockResponse(
                stock.getId(),
                stock.getName(),
                stock.getType(),
                stock.getVariants().stream()
                        .map(this::toVariantResponse)
                        .collect(Collectors.toList()));
    }

    public StockResponse.VariantResponse toVariantResponse(StockVariant variant) {
        return new StockResponse.VariantResponse(
                variant.getId(),
                variant.getName(),
                variant.getBaseUnit(),
                variant.getOpeningStock(),
                variant.getCurrentStock(),
                variant.getUnits().stream()
                        .map(this::toUnitResponse)
                        .collect(Collectors.toList()));
    }

    private StockResponse.UnitResponse toUnitResponse(VariantUnit unit) {
        return new StockResponse.UnitResponse(
                unit.getId(),
                unit.getName(),
                unit.getConversionRate(),
                unit.getUnitType());
    }
}