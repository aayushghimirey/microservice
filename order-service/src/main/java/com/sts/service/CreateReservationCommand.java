package com.sts.command;


import java.util.List;
import java.util.UUID;

public record CreateReservationCommand(
        UUID tableId,
        List<ReservationItemCommand> items

) {

    public record ReservationItemCommand(
            UUID menuId,
            int quantity
    ) {
    }

}
