package com.sts.dto.request;

import com.sts.constant.enums.StockType;

public record GetStockQueryRequest(
        String name,
        StockType type
) {
}
