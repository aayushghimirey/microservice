package com.sts.utils;

import com.sts.dto.response.ReservationResponse;
import com.sts.mapper.ReservationMapper;
import com.sts.repository.ReservationRepository;
import com.sts.service.ReservationSseService;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PushPendingReservations {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationSseService reservationSseService;

    public void pushPendingReservations() {

        List<ReservationResponse> list = reservationRepository
                .findByStatus(ReservationStatus.PENDING)
                .stream()
                .map(reservationMapper::toResponse)
                .toList();

        reservationSseService.sendUpdate(
                AppConstants.TEST_TENANT,
                list
        );
    }
}
