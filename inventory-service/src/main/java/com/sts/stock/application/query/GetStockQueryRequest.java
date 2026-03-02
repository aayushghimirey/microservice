package com.sts.stock.application.query;

import com.sts.stock.domain.enums.StockType;

public record GetStockQueryRequest(
        String name,
        StockType type
) {
}
