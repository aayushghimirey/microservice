package com.sts.controller;

import com.sts.pagination.PageRequestDto;
import com.sts.service.ReservationSseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sts.dto.request.CreateReservationCommand;
import com.sts.dto.response.ReservationResponse;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.ReservationService;
import com.sts.utils.contant.AppConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(AppConstants.RESERVATION_BASE_PATH)
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationSseService reservationSseService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @RequestBody CreateReservationCommand request) {
        log.info(AppConstants.LOG_MESSAGES.CREATING_RESERVATION, request.tableId());
        return AppResponse.success(reservationService.createReservation(request),
                AppConstants.SUCCESS_MESSAGES.RESERVATION_CREATED);
    }

    @GetMapping(value = "/orders/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getOrderStream(@RequestParam UUID tenantId) {

        SseEmitter emitter = reservationSseService.createEmitter(tenantId);

        reservationSseService.initPush(
                tenantId,
                reservationService.getAllPendingOrders()
        );

        return emitter;
    }
}
