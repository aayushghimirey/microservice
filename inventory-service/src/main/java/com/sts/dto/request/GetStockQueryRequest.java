package com.sts.dto.request;

import com.sts.utils.enums.StockType;

public record GetStockQueryRequest(
        String name,
        StockType type
) {
}
