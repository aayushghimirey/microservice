package com.sts.dto.response;

public record ReservationOrderInfo(
        int orderCount,
        int cancelledCount,
        int successOrderCount
) {
}
