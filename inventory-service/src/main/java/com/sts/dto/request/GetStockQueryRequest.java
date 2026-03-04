package com.sts.dto;

import com.sts.constant.enums.StockType;

public record GetStockQueryRequest(
        String name,
        StockType type
) {
}
