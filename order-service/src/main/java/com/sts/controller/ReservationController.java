package com.sts.controller;

import com.sts.dto.request.CreateReservationCommand;
import com.sts.dto.response.ReservationResponse;
import com.sts.model.Reservation;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.ReservationService;
import com.sts.service.ReservationSseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationSseService reservationSseService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(@RequestBody CreateReservationCommand request) {
        return AppResponse.success(reservationService.createReservation(request), "Reservation Created");
    }

    /*
     * Sse event with init order list and new order
     * */
    @GetMapping("/orders/stream")
    public SseEmitter stream(UUID tenantId) {

        SseEmitter emitter = reservationSseService.createEmitter(tenantId);

        reservationSseService.initPush(tenantId);

        return emitter;
    }

}
