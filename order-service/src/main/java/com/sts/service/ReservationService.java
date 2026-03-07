package com.sts.service;

import com.sts.dto.request.CreateReservationCommand;
import com.sts.dto.response.ReservationResponse;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.MenuIngredientResponse;
import com.sts.event.MenuResponse;

import com.sts.event.OrderCreatedEvent;
import com.sts.exception.MenuNotFoundException;
import com.sts.exception.TableNotFoundException;
import com.sts.exception.TableNotOpenException;
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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final ReservationMapper reservationMapper;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;
    private final MenuClient menuClient;

    @Transactional
    public ReservationResponse createReservation(CreateReservationCommand request) {

        Table table = tableRepository.findById(request.tableId()).orElseThrow(
                () -> new TableNotFoundException(String.format(AppConstants.TABLE_NOT_FOUND, request.tableId())));

        if (!table.getStatus().equals(TableStatus.OPEN)) {
            throw new TableNotOpenException(String.format(AppConstants.TABLE_NOT_OPEN));
        }

        Reservation reservation = buildReservation(request, table);

        publishOrderCreatedEvent(reservation);

        // reserve table
        table.setStatus(TableStatus.RESERVED);

        // send sse event


        tableRepository.save(table);

        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getAllPendingOrders() {
        return reservationRepository.findByStatus(ReservationStatus.PENDING).stream().map(
                reservationMapper::toResponse
        ).toList();
    }


    public List<ReservationResponse.OrderItem> getOrderItemBySessionId(UUID sessionId) {

        return reservationRepository.findBySessionId(sessionId)
                .stream()
                .map(Reservation::getReservationOrders)
                .flatMap(List::stream)
                .map(reservationMapper::toOrderItem)
                .toList();

    }


    /*
     * private helpers
     * */

    private Reservation buildReservation(CreateReservationCommand request, Table table) {

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setSessionId(UUID.randomUUID());
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);

        for (var itemRequest : request.items()) {

            MenuResponse menuResponseDto = menuClient.getMenuById(itemRequest.menuId()).getBody().getData();

            if (menuResponseDto == null) {
                throw new MenuNotFoundException(String.format(AppConstants.MENU_NOT_FOUND, itemRequest.menuId()));
            }

            ReservationOrders orders = buildReservationOrders(itemRequest, menuResponseDto);
            reservation.addReservationOrder(orders);
        }

        return reservation;
    }

    private ReservationOrders buildReservationOrders(CreateReservationCommand.ReservationItemRequest request,
                                                     MenuResponse menuResponseDto) {
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

            List<OrderCreatedEvent.MenuItems> menuItems = reservation.getReservationOrders().stream()
                    .flatMap(order -> buildMenuItems(order).stream())
                    .toList();

            orderCreatedEvent.setItems(menuItems);

            String payload = objectMapper.writeValueAsString(orderCreatedEvent);

            OutboxEvent outboxEvent = outboxMapper.map(
                    "ORDER",
                    reservation.getId().toString(),
                    OutboxEventType.CREATED,
                    payload,
                    kafkaProperties.getTopic("order-event"));

            outboxEventRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("Error publishing order created event", e);
        }

    }

    private List<OrderCreatedEvent.MenuItems> buildMenuItems(ReservationOrders reservationOrders) {

        List<MenuIngredientResponse> ingredients = menuClient.getMenuIngredentsById(reservationOrders.getMenuItemId()).getBody().getData();

        return ingredients.stream().map(
                ingredient ->
                        new OrderCreatedEvent.MenuItems(
                                ingredient.getVariantId(),
                                ingredient.getUnitId(),
                                ingredient.getQuantity()
                        )
        ).toList();
    }

}
