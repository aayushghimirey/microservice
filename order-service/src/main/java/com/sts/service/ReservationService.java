package com.sts.service;

import com.sts.dto.request.ReservationRequest;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.MenuResponseDto;
import com.sts.event.OrderCreatedEvent;
import com.sts.mapper.OutboxMapper;
import com.sts.model.Reservation;
import com.sts.model.ReservationOrders;
import com.sts.model.Table;
import com.sts.repository.OutboxEventRepository;
import com.sts.repository.ReservationRepository;
import com.sts.repository.TableRepository;
import com.sts.topics.KafkaProperties;
import com.sts.topics.KafkaTopics;
import com.sts.utils.enums.ReservationStatus;
import com.sts.utils.feign.MenuClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;
    private final MenuClient menuClient;

    @Transactional
    public Reservation createReservation(ReservationRequest request) {

        Table table = tableRepository.findById(request.tableId()).orElseThrow(
                () -> new IllegalArgumentException("Table not found with id: " + request.tableId()));

        Reservation reservation = buildReservation(request, table);

        publishOrderCreatedEvent(reservation);

        return reservationRepository.save(reservation);
    }

    private Reservation buildReservation(ReservationRequest request, Table table) {

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setSessionId(UUID.randomUUID());
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);

        for (var itemRequest : request.items()) {

            MenuResponseDto menuResponseDto = menuClient.getMenuById(itemRequest.menuId()).getBody();

            if (menuResponseDto == null) {
                throw new RuntimeException("Invalid menu id");
            }

            ReservationOrders orders = buildReservationOrders(itemRequest, menuResponseDto);
            reservation.addReservationOrder(orders);
        }

        return reservation;
    }

    private ReservationOrders buildReservationOrders(ReservationRequest.ReservationItemRequest request,
            MenuResponseDto menuResponseDto) {
        ReservationOrders orders = new ReservationOrders();
        orders.setMenuItemId(request.menuId());
        orders.setQuantity(request.quantity());
        orders.setPrice(menuResponseDto.getPrice());
        return orders;
    }

    private void publishOrderCreatedEvent(Reservation reservation) {
        try {
            OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
            orderCreatedEvent.setReservationId(reservation.getId());
            orderCreatedEvent.setSessionId(reservation.getSessionId());
            orderCreatedEvent.setStatus(reservation.getStatus().name());
            orderCreatedEvent.setTableId(reservation.getTable().getId());
            orderCreatedEvent.setBillAmount(reservation.getBillAmount());
            orderCreatedEvent.setReservationTime(reservation.getReservationTime());

            String payload = objectMapper.writeValueAsString(orderCreatedEvent);

            OutboxEvent outboxEvent = outboxMapper.map(
                    "ORDER",
                    reservation.getId().toString(),
                    OutboxEventType.CREATED,
                    payload,
                    kafkaProperties.getOrderEvent());

            outboxEventRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("Error publishing order created event", e);
        }

    }

}
