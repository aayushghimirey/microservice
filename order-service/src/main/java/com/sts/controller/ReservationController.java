package com.sts.controller;


import com.sts.pagination.PageRequestDto;
import com.sts.response.PagedResponse;
import com.sts.utils.enums.ReservationStatus;
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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(AppConstants.RESERVATION_BASE_PATH)
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @RequestBody CreateReservationCommand request) {

        log.info("Request received: createReservation tableId={} time={}",
                request.tableId(),
                LocalDateTime.now());

        ReservationResponse response =
                reservationService.createReservation(request);

        log.info("Request completed: createReservation sessionId={}",
                response.sessionId());

        return AppResponse.success(
                response,
                AppConstants.SUCCESS_MESSAGES.RESERVATION_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<ReservationResponse>>> getAllReservations(
            @RequestParam ReservationStatus status,
            @ModelAttribute PageRequestDto pageRequestDto) {

        log.info("Request received: getAllReservations status={} page={} size={}",
                status,
                pageRequestDto.getPage(),
                pageRequestDto.getSize());

        var response =
                reservationService.getAllReservationByStatus(
                        status,
                        pageRequestDto.buildPageable());

        log.info("Request completed: getAllReservations status={} totalElements={}",
                status,
                response.getTotalElements());

        return AppResponse.success(
                response,
                AppConstants.SUCCESS_MESSAGES.RESERVATION_FETCHED);
    }
}