package com.sts.utils.enums;

import java.math.BigDecimal;

public enum StockUpdateSource {
    PURCHASE(1),
    ADJUSTMENT(1),
    SALE(-1),
    RETURN(1);

    private final int multiplier;

    StockUpdateSource(int multiplier) {
        this.multiplier = multiplier;
    }

    public BigDecimal getMultiplier() {
        return BigDecimal.valueOf(multiplier);
    }
}