package com.sts.mapper;

import com.sts.dto.response.ReservationResponse;
import com.sts.model.Reservation;
import com.sts.model.ReservationOrders;
import org.springframework.stereotype.Component;


@Component
public class ReservationMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse reservationResponse = ReservationResponse.builder()
                .sessionId(reservation.getSessionId())
                .reservationTime(reservation.getReservationTime())
                .reservationEndTime(reservation.getReservationEndTime())
                .status(reservation.getStatus())
                .billAmount(reservation.getBillAmount())
                .tableId(reservation.getTable().getId())
                .build();

        for (var order : reservation.getReservationOrders()) {
            reservationResponse.items().add(this.toOrderItem(order));
        }

        return reservationResponse;

    }

    public ReservationResponse.OrderItem toOrderItem(ReservationOrders reservationOrder) {
        return ReservationResponse.OrderItem.builder()
                .menuItemId(reservationOrder.getMenuItemId())
                .price(reservationOrder.getPrice())
                .quantity(reservationOrder.getQuantity())
                .build();
    }



}
