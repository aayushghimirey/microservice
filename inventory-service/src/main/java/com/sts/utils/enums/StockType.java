package com.sts.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StockType {
    FOOD,
    BEVERAGE,
    OTHER;

    @JsonCreator
    public static StockType from(String value) {
        try {
            return StockType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid StockType: " + value);
        }
    }

    }
