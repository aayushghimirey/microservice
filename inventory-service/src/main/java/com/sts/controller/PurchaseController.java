package com.sts.controller;

import java.util.List;

import com.sts.utils.constant.AppConstants;
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
     * Creates a new purchase. VAT (13%) is automatically calculated and added to the sub-total
     * to derive the final Gross Total.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseResponse>> createPurchase(
            @Valid @RequestBody CreatePurchaseCommand command) {

        log.info(AppConstants.LOG_MESSAGES.CREATING_PURCHASE, command.invoiceNumber());

        return AppResponse.success(purchaseService.createPurchase(command), AppConstants.SUCCESS_MESSAGES.PURCHASE_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<PurchaseResponse>>> getAllPurchases(
            PageRequestDto pageRequestDto) {

        log.info(AppConstants.LOG_MESSAGES.FETCHING_PURCHASE, pageRequestDto.getPage(), pageRequestDto.getSize());

        return AppResponse.success(purchaseService.getAllPurchases(pageRequestDto.buildPageable()),
                AppConstants.SUCCESS_MESSAGES.PURCHASE_FETCHED);
    }

}
