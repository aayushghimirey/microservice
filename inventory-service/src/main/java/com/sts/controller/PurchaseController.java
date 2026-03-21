package com.sts.controller;

import java.util.List;

import com.sts.utils.constant.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.PurchaseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(AppConstants.PURCHASE_BASE_PATH)
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * Creates a new purchase. VAT (13%) is automatically calculated and added to
     * the sub-total
     * to derive the final Gross Total.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseResponse>> createPurchase(
            @Valid @RequestBody CreatePurchaseCommand command) {

        PurchaseResponse purchase = purchaseService.createPurchase(command);

        return AppResponse.success(
                purchase,
                AppConstants.Response.PURCHASE_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<PurchaseResponse>>> getAllPurchases(
            PageRequestDto pageRequestDto) {

        Page<PurchaseResponse> purchases = purchaseService.getAllPurchases(
                pageRequestDto.buildPageable());

        return AppResponse.success(
                purchases,
                AppConstants.Response.FETCHED_PURCHASES);
    }

}
