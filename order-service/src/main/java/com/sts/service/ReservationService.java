package com.sts.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sts.dto.request.UpdateOrderItemCommand;
import com.sts.dto.response.ReservationOrderInfo;
import com.sts.enums.DateSelection;
import com.sts.filter.TenantHolder;
import com.sts.utils.OrderOutboxPublisher;
import com.sts.utils.feign.MenuClientGateway;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.request.CreateReservationCommand;
import com.sts.dto.response.ReservationResponse;


import com.sts.event.MenuResponse;

import com.sts.exception.BusinessValidationException;
import com.sts.exception.ResourceNotFoundException;

import com.sts.mapper.ReservationMapper;
import com.sts.model.Reservation;
import com.sts.model.ReservationOrders;
import com.sts.model.Table;
import com.sts.repository.ReservationRepository;
import com.sts.repository.TableRepository;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.ReservationStatus;
import com.sts.utils.enums.TableStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    private final ReservationMapper reservationMapper;

    private final OrderOutboxPublisher orderOutboxPublisher;

    private final MenuClientGateway menuClientGateway;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RlsContext rlsContext;

    @Transactional
    public ReservationResponse createReservation(CreateReservationCommand request) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Table table = validateTable(request.tableId());

        Map<UUID, MenuResponse> menuMap = request.items().stream().map(item ->
                fetchMenu(item.menuId())).collect(java.util.stream.Collectors.toMap(MenuResponse::getId, menu -> menu));

        Reservation reservation = buildReservation(request, table, menuMap);

        reservation = reservationRepository.save(reservation);

        orderOutboxPublisher.publish(reservation, menuMap);

        reserveTable(table);


        log.info(AppConstants.LOG_MESSAGES.RESERVATION_CREATED, reservation.getId());

        ReservationResponse response = reservationMapper.toResponse(reservation);

        simpMessagingTemplate.convertAndSendToUser(reservation.getTenantId().toString(), "/queue/orders", response);

        return response;
    }

    @Transactional
    public ReservationResponse updateReservation(UUID sessionId, UpdateOrderItemCommand itemCommand) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Reservation prevReservation = reservationRepository.findBySessionId(sessionId);

        // clear all prev items
        prevReservation.getReservationOrders().clear();

        // build new items
        Map<UUID, MenuResponse> menuMap = itemCommand.items().stream().map(item ->
                fetchMenu(item.menuId())).collect(java.util.stream.Collectors.toMap(MenuResponse::getId, menu -> menu));

        for (var itemRequest : itemCommand.items()) {

            MenuResponse menuResponse = menuMap.get(itemRequest.menuId());
            ReservationOrders orders = reservationMapper.buildReservationOrders(itemRequest, menuResponse);

            prevReservation.addReservationOrder(orders);
        }

        prevReservation.setStatus(ReservationStatus.UPDATED);

        Reservation reservation = reservationRepository.save(prevReservation);

        orderOutboxPublisher.publish(reservation, menuMap);

        ReservationResponse response = reservationMapper.toResponse(reservation);

        simpMessagingTemplate.convertAndSendToUser(reservation.getTenantId().toString(), "/queue/orders", response);

        return response;

    }

    @Transactional
    public void cancelReservation(UUID sessionId) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Reservation reservation = reservationRepository.findBySessionId(sessionId);

        if (reservation == null) {
            throw new ResourceNotFoundException("Reservation not found for sessionId: " + sessionId);
        }


        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.getTable().setStatus(TableStatus.OPEN);

        ReservationResponse response = reservationMapper.toResponse(reservation);

        orderOutboxPublisher.publishOrderCancelled(reservation);

        simpMessagingTemplate.convertAndSendToUser(reservation.getTenantId().toString(), "/queue/orders", response);


        reservationRepository.save(reservation);
    }


    @Transactional
    public Page<ReservationResponse> getAllReservationByStatus(ReservationStatus status, Pageable pageable) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return reservationRepository.findByStatus(status, pageable).map(reservationMapper::toResponse);
    }

    @Transactional
    public ReservationOrderInfo reservationOrderInfo(DateSelection dateSelection) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();


        ZoneId zone = ZoneId.of("Asia/Kathmandu");

        Instant from = switch (dateSelection) {

            case TODAY -> LocalDate.now(zone)
                    .atStartOfDay(zone)
                    .toInstant();

            case WEEK -> Instant.now().minus(7, ChronoUnit.DAYS);

            case MONTH -> Instant.now().minus(30, ChronoUnit.DAYS);
        };

        List<Object[]> results =
                reservationRepository.countByStatusFrom(from);

        Map<ReservationStatus, Integer> map = new EnumMap<>(ReservationStatus.class);

        for (Object[] row : results) {
            map.put(
                    (ReservationStatus) row[0],
                    ((Long) row[1]).intValue()
            );
        }
        int pending = map.getOrDefault(ReservationStatus.PENDING, 0);
        int updated = map.getOrDefault(ReservationStatus.UPDATED, 0);
        int cancelled = map.getOrDefault(ReservationStatus.CANCELLED, 0);
        int completed = map.getOrDefault(ReservationStatus.COMPLETED, 0);

        int total = pending + updated + cancelled + completed;

        return new ReservationOrderInfo(
                total,
                cancelled,
                completed
        );

    }

    /*
     * --------------------------------------------------
     * Private helpers
     * --------------------------------------------------
     */

    private Table validateTable(UUID tableId) {

        Table table = tableRepository.findById(tableId).orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.ERROR_MESSAGES.TABLE_NOT_FOUND, tableId)));

        if (!TableStatus.OPEN.equals(table.getStatus())) {
            throw new BusinessValidationException(AppConstants.ERROR_MESSAGES.TABLE_NOT_OPEN);
        }

        return table;
    }

    private void reserveTable(Table table) {
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);
    }

    private Reservation buildReservation(CreateReservationCommand request, Table table, Map<UUID, MenuResponse> menuMap) {

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setSessionId(UUID.randomUUID());
        reservation.setReservationTime(Instant.now());
        reservation.setStatus(ReservationStatus.PENDING);

        for (var itemRequest : request.items()) {

            MenuResponse menu = menuMap.get(itemRequest.menuId());

            ReservationOrders orders = reservationMapper.buildReservationOrders(itemRequest, menu);

            reservation.addReservationOrder(orders);
        }

        return reservation;
    }

    private MenuResponse fetchMenu(UUID menuId) {

        return menuClientGateway.getMenuById(menuId);

    }


}