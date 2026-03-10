package com.sts.controller;

import java.util.List;

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
import com.sts.utils.contant.AppConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(AppConstants.PURCHASE_BASE_PATH)
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    /*
     * In purchase , vat amount should not be included, it will be auto calculated.
     * Like eg:
     * Got vat bill,
     * item A - price 500
     * item B - price 600
     *
     * Bill type is vat.
     * Mean automatically vat 13 % will be added
     * like 500 + 600 = 1100 this is sub-total.
     * now system will add 13 % vat on 1100 which is Rs.143
     * Therefore: Gross Total is subTotal + vat = 1100 + 143 = 1,243
     *
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
