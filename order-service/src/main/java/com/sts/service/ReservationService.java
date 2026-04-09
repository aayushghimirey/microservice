package com.sts.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

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

        Map<UUID, MenuResponse> menuMap = request.items().stream()
                .map(item -> fetchMenu(item.menuId()))
                .collect(java.util.stream.Collectors.toMap(MenuResponse::getId, menu -> menu));

        Reservation reservation = buildReservation(request, table, menuMap);

        reservation = reservationRepository.save(reservation);

        orderOutboxPublisher.publish(reservation, menuMap);

        reserveTable(table);


        log.info(AppConstants.LOG_MESSAGES.RESERVATION_CREATED, reservation.getId());

        ReservationResponse response = reservationMapper.toResponse(reservation);

        simpMessagingTemplate.convertAndSend("/topic/orders", response);

        return response;
    }

    public Page<ReservationResponse> getAllReservationByStatus(ReservationStatus status, Pageable pageable) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return reservationRepository.findByStatus(status, pageable).map(reservationMapper::toResponse);
    }

    /*
     * --------------------------------------------------
     * Private helpers
     * --------------------------------------------------
     */

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

    private Reservation buildReservation(CreateReservationCommand request, Table table, Map<UUID, MenuResponse> menuMap) {

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setSessionId(UUID.randomUUID());
        reservation.setReservationTime(LocalDateTime.now());
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