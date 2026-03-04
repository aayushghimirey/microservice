package com.sts.command;

import com.sts.domain.enums.ReservationStatus;
import com.sts.domain.model.Reservation;
import com.sts.domain.model.ReservationOrders;
import com.sts.domain.model.Table;
import com.sts.event.MenuResponseDto;
import com.sts.infra.feign.FeignMenuValidatorClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ReservationCommandHandler {

    private final FeignMenuValidatorClient validatorClient;

    public Reservation buildReservation(CreateReservationCommand command, Table table) {

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setSessionId(UUID.randomUUID());
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);

        for (var itemCommand : command.items()) {

            MenuResponseDto menuResponseDto = validatorClient.getMenuById(itemCommand.menuId()).getBody();

            if (menuResponseDto == null) {
                throw new RuntimeException("Invalid menu id");
            }

            ReservationOrders orders = buildReservationOrders(itemCommand, menuResponseDto);
            reservation.addReservationOrder(orders);
        }

        return reservation;
    }


    private ReservationOrders buildReservationOrders(CreateReservationCommand.ReservationItemCommand command, MenuResponseDto menuResponseDto) {
        ReservationOrders orders = new ReservationOrders();
        orders.setMenuItemId(command.menuId());
        orders.setQuantity(command.quantity());
        orders.setPrice(menuResponseDto.getPrice());
        return orders;
    }

}
