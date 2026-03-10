package com.sts.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sts.dto.response.ReservationResponse;
import com.sts.mapper.ReservationMapper;
import com.sts.repository.ReservationRepository;
import com.sts.service.ReservationSseService;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class PushPendingReservations {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationSseService reservationSseService;

    public void pushPendingReservations() {
        log.debug("Pushing pending reservations via SSE");

        List<ReservationResponse> list = reservationRepository
                .findByStatus(ReservationStatus.PENDING)
                .stream()
                .map(reservationMapper::toResponse)
                .toList();

        log.debug("Found {} pending reservations to push", list.size());

        reservationSseService.sendUpdate(
                AppConstants.TEST_TENANT,
                list);
    }
}
