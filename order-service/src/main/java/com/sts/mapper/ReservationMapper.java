package com.sts.mapper;

import com.sts.dto.request.CreateReservationCommand;
import com.sts.dto.request.UpdateOrderItemCommand;
import com.sts.dto.response.ReservationResponse;
import com.sts.event.MenuResponse;
import com.sts.model.Reservation;
import com.sts.model.ReservationOrders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ReservationMapper {


    public ReservationOrders buildReservationOrders(CreateReservationCommand.ReservationItemRequest request,
                                                    MenuResponse menuResponseDto) {
        ReservationOrders orders = new ReservationOrders();
        orders.setMenuItemId(request.menuId());
        orders.setMenuItemName(menuResponseDto.getName());
        orders.setQuantity(request.quantity());
        orders.setPrice(menuResponseDto.getPrice());
        return orders;
    }

    public ReservationOrders buildReservationOrders(UpdateOrderItemCommand.UpdateReservationItemRequest request,
                                                    MenuResponse menuResponseDto) {
        ReservationOrders orders = new ReservationOrders();
        orders.setMenuItemId(request.menuId());
        orders.setMenuItemName(menuResponseDto.getName());
        orders.setQuantity(request.quantity());
        orders.setPrice(menuResponseDto.getPrice());
        return orders;
    }


    public ReservationResponse toResponse(Reservation reservation) {

        List<ReservationResponse.OrderItem> items = new ArrayList<>();

        for (var order : reservation.getReservationOrders()) {
            items.add(this.toOrderItemResponse(order));
        }

        return ReservationResponse.builder()
                .sessionId(reservation.getSessionId())
                .reservationTime(reservation.getReservationTime())
                .reservationEndTime(reservation.getReservationEndTime())
                .status(reservation.getStatus())
                .billAmount(reservation.getBillAmount())
                .tableId(reservation.getTable().getId())
                .items(items)
                .build();
    }

    public ReservationResponse.OrderItem toOrderItemResponse(ReservationOrders reservationOrder) {
        return ReservationResponse.OrderItem.builder()
                .menuItemId(reservationOrder.getMenuItemId())
                .price(reservationOrder.getPrice())
                .quantity(reservationOrder.getQuantity())
                .build();
    }


}
