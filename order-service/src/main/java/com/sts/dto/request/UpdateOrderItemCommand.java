package com.sts.dto.request;

import java.util.List;
import java.util.UUID;

public record UpdateOrderItemCommand(

        List<UpdateReservationItemRequest> items

) {

    public record UpdateReservationItemRequest(
            UUID menuId,
            int quantity) {
    }


}
