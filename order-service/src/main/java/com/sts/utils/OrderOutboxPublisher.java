package com.sts.utils;

import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.event.MenuResponse;
import com.sts.event.OrderCreatedEvent;
import com.sts.filter.TenantHolder;
import com.sts.helper.outbox.OutboxPublisher;
import com.sts.model.Reservation;
import com.sts.topics.KafkaProperties;
import com.sts.utils.contant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class OrderOutboxPublisher {

    private final OutboxPublisher outboxPublisher;
    private final KafkaProperties kafkaProperties;

    public void publish(Reservation reservation, Map<UUID, MenuResponse> menuItemMap) {

        OrderCreatedEvent event = buildOrderCreatedEvent(reservation, menuItemMap);


        outboxPublisher.publish(
                AggregateType.ORDER,
                event.getSessionId(),
                OutboxEventType.CREATED,
                event,
                kafkaProperties.getTopic(AppConstants.ORDER_KAFKA_EVENT_TOPIC)
        );

    }

    public void publishOrderCancelled(Reservation reservation) {

        OrderCreatedEvent event = new OrderCreatedEvent();

        event.setReservationId(reservation.getId());
        event.setTenantId(TenantHolder.getTenantId());
        event.setSessionId(reservation.getSessionId());
        event.setStatus(reservation.getStatus().name());
        event.setTableId(reservation.getTable().getId());
        event.setBillAmount(reservation.getBillAmount());
        event.setReservationTime(reservation.getReservationTime());

        outboxPublisher.publish(
                AggregateType.ORDER,
                event.getSessionId(),
                OutboxEventType.DELETED,
                event,
                kafkaProperties.getTopic(AppConstants.ORDER_KAFKA_EVENT_TOPIC)
        );

    }

    private OrderCreatedEvent buildOrderCreatedEvent(Reservation reservation, Map<UUID, MenuResponse> menuItemMap) {

        OrderCreatedEvent event = new OrderCreatedEvent();

        event.setReservationId(reservation.getId());
        event.setTenantId(TenantHolder.getTenantId());
        event.setSessionId(reservation.getSessionId());
        event.setStatus(reservation.getStatus().name());
        event.setTableId(reservation.getTable().getId());
        event.setBillAmount(reservation.getBillAmount());
        event.setReservationTime(reservation.getReservationTime());

        List<OrderCreatedEvent.MenuItem> items = reservation.getReservationOrders().stream()
                .map(item -> new OrderCreatedEvent.MenuItem(
                        item.getMenuItemId(),
                        item.getQuantity(),
                        menuItemMap.get(item.getMenuItemId()).getIngredients()
                )).toList();


        event.setItems(items);

        return event;
    }


}
