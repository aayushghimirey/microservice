package com.sts.dto.request;

import java.util.List;
import java.util.UUID;

public record ReservationRequest(
        UUID tableId,
        List<ReservationItemRequest> items

) {

    public record ReservationItemRequest(
            UUID menuId,
            int quantity) {
    }

}
