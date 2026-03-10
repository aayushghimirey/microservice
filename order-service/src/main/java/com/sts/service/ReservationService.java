package com.sts.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.sts.utils.PushPendingReservations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.request.CreateReservationCommand;
import com.sts.dto.response.ReservationResponse;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.MenuResponse;
import com.sts.event.OrderCreatedEvent;
import com.sts.exception.BusinessValidationException;
import com.sts.exception.ResourceNotFoundException;
import com.sts.mapper.OutboxMapper;
import com.sts.mapper.ReservationMapper;
import com.sts.model.Reservation;
import com.sts.model.ReservationOrders;
import com.sts.model.Table;
import com.sts.repository.OutboxEventRepository;
import com.sts.repository.ReservationRepository;
import com.sts.repository.TableRepository;
import com.sts.topics.KafkaProperties;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.ReservationStatus;
import com.sts.utils.enums.TableStatus;
import com.sts.utils.feign.MenuClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final ReservationMapper reservationMapper;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;
    private final MenuClient menuClient;

    private final PushPendingReservations pushPendingReservations;

    private final ReservationSseService reservationSseService;

    @Transactional
    public ReservationResponse createReservation(CreateReservationCommand request) {

        Table table = validateTable(request.tableId());

        Reservation reservation = buildReservation(request, table);

        reservation = reservationRepository.save(reservation);

        createReservationOutboxEvent(reservation);

        reserveTable(table);

        pushPendingReservations.pushPendingReservations();

        log.info(AppConstants.LOG_MESSAGES.RESERVATION_CREATED, reservation.getId());

        return reservationMapper.toResponse(reservation);
    }




    /* --------------------------------------------------
       Private helpers
     -------------------------------------------------- */

    private Table validateTable(UUID tableId) {

        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(AppConstants.ERROR_MESSAGES.TABLE_NOT_FOUND, tableId)));

        if (!TableStatus.OPEN.equals(table.getStatus())) {
            throw new BusinessValidationException(AppConstants.ERROR_MESSAGES.TABLE_NOT_OPEN);
        }

        return table;
    }

    private void reserveTable(Table table) {
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);
    }

    private Reservation buildReservation(CreateReservationCommand request, Table table) {

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setSessionId(UUID.randomUUID());
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);

        for (var itemRequest : request.items()) {

            MenuResponse menu = fetchMenu(itemRequest.menuId());

            ReservationOrders orders =
                    reservationMapper.buildReservationOrders(itemRequest, menu);

            reservation.addReservationOrder(orders);
        }

        return reservation;
    }

    private MenuResponse fetchMenu(UUID menuId) {

        var response = menuClient.getMenuById(menuId);

        if (response == null || response.getBody() == null || response.getBody().getData() == null) {
            throw new ResourceNotFoundException(
                    String.format(AppConstants.ERROR_MESSAGES.MENU_NOT_FOUND, menuId));
        }

        return response.getBody().getData();
    }

    private void createReservationOutboxEvent(Reservation reservation) {

        try {
            OrderCreatedEvent event = buildOrderCreatedEvent(reservation);

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = outboxMapper.map(
                    "ORDER",
                    reservation.getId().toString(),
                    OutboxEventType.CREATED,
                    payload,
                    kafkaProperties.getTopic("order-event"));

            outboxEventRepository.save(outboxEvent);

        } catch (Exception e) {
            log.error("Failed to create reservation outbox event", e);
            throw new RuntimeException("Error publishing order created event", e);
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Reservation reservation) {

        OrderCreatedEvent event = new OrderCreatedEvent();

        event.setReservationId(reservation.getId());
        event.setSessionId(reservation.getSessionId());
        event.setStatus(reservation.getStatus().name());
        event.setTableId(reservation.getTable().getId());
        event.setBillAmount(reservation.getBillAmount());
        event.setReservationTime(reservation.getReservationTime());

        List<OrderCreatedEvent.MenuItem> items = reservation.getReservationOrders().stream()
                .map(item -> new OrderCreatedEvent.MenuItem(
                        item.getMenuItemId(),
                        item.getQuantity()
                )).toList();

        event.setItems(items);

        return event;
    }


}