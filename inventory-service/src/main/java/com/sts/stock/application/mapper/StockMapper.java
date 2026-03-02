package com.sts.stock.application.mapper;

import java.util.stream.Collectors;

import com.sts.stock.domain.model.Stock;
import com.sts.stock.domain.model.StockVariant;
import com.sts.stock.domain.model.VariantUnit;
import org.springframework.stereotype.Component;

import com.sts.stock.dto.StockResponse;

@Component
public class StockMapper {

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