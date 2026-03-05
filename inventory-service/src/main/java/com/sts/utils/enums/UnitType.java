package com.sts.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UnitType {
    SELL, PURCHASE, BOTH;


    @JsonCreator
    public static UnitType from(String value) {
        try {
            return UnitType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UnitType: " + value);
        }
    }
}
