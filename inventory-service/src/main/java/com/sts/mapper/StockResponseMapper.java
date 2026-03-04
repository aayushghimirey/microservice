package com.sts.mapper;

import java.util.stream.Collectors;

import com.sts.entity.stock.Stock;
import com.sts.entity.stock.StockVariant;
import com.sts.entity.stock.VariantUnit;
import org.springframework.stereotype.Component;

import com.sts.dto.response.StockResponse;

 public final class StockMapper {


     StockMapper() {

     }

    public static StockResponse toResponse(Stock stock) {
        if (stock == null)
            return null;

        return new StockResponse(
                stock.getId(),
                stock.getName(),
                stock.getType(),
                stock.getVariants().stream()
                        .map(StockMapper::toVariantResponse)
                        .collect(Collectors.toList()));
    }

    public static StockResponse.VariantResponse toVariantResponse(StockVariant variant) {
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