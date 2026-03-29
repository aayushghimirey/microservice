package com.sts.mapper;

import java.math.BigDecimal;

import com.sts.domain.Audit;
import com.sts.event.StockEvent;
import org.springframework.stereotype.Component;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;

@Component
public class StockMapper {

    // -------- entity construction --------
    public Stock buildStock(CreateStockCommand command) {
        Stock stock = Stock.builder()
                .name(command.name())
                .type(command.type())
                .build();

        // adding variants
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

        // adding units for variant
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

    // -------- entity to response mapping --------
    public StockResponse toResponse(Stock stock) {
        if (stock == null)
            return null;

        return new StockResponse(
                stock.getId(),
                stock.getName(),
                stock.getType(),
                stock.getVariants().stream()
                        .map(this::toVariantResponse)
                        .toList());
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
                        .toList());
    }

    private StockResponse.UnitResponse toUnitResponse(VariantUnit unit) {
        return new StockResponse.UnitResponse(
                unit.getId(),
                unit.getName(),
                unit.getConversionRate(),
                unit.getUnitType());
    }

    // -------- entity event mapping --------
    public StockEvent toStockEvent(Stock stock) {
        return new StockEvent(
                stock.getId(),
                stock.getVariants().stream()
                        .map(this::toVariantEvent)
                        .toList());

    }

    private StockEvent.VariantStockEvent toVariantEvent(StockVariant variant) {
        return new StockEvent.VariantStockEvent(
                variant.getId(),
                variant.getUnits().stream()
                        .map(Audit::getId)
                        .toList()
        );

    }


}