package com.sts.dto.response;

import com.sts.utils.enums.ReservationStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
public record ReservationResponse(
        UUID sessionId,
        Instant reservationTime,
        Instant reservationEndTime,
        ReservationStatus status,
        BigDecimal billAmount,
        UUID tableId,
        List<OrderItem> items
) {

    public ReservationResponse {
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    @Builder
    public record OrderItem(
            UUID menuItemId,
            BigDecimal price,
            Integer quantity
    ) {
    }
}