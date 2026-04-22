package com.sts.controller;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.request.GetPurchaseQueryRequest;
import com.sts.dto.response.PurchaseInfo;
import com.sts.dto.response.PurchaseResponse;
import com.sts.enums.DateSelection;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.PurchaseService;
import com.sts.utils.constant.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(AppConstants.PURCHASE_BASE_PATH)
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * Creates a new purchase. VAT (13%) is automatically calculated and added to
     * the sub-total to derive the final Gross Total.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseResponse>> createPurchase(
            @Valid @RequestBody CreatePurchaseCommand command) {

        log.info("Request received: createPurchase vendorId={} totalItems={}",
                command.vendorId(),
                command.items() != null ? command.items().size() : 0);

        PurchaseResponse purchase = purchaseService.createPurchase(command);

        log.info("Request completed: createPurchase purchaseId={} grossTotal={}",
                purchase.id(),
                purchase.grossTotal());

        return AppResponse.success(
                purchase,
                AppConstants.Response.PURCHASE_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<PurchaseResponse>>> getAllPurchases(
            GetPurchaseQueryRequest request,
            PageRequestDto pageRequestDto) {

        log.info("Request received: getAllPurchases page={} size={}",
                pageRequestDto.getPage(),
                pageRequestDto.getSize());

        var purchases =
                purchaseService.getAllPurchases(request, pageRequestDto.buildPageable());

        log.info("Request completed: getAllPurchases totalElements={}",
                purchases.getTotalElements());

        return AppResponse.success(
                purchases,
                AppConstants.Response.FETCHED_PURCHASES);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<PurchaseInfo>> getPurchaseInfo(DateSelection dateSelection) {
        return AppResponse.success(purchaseService.getPurchaseDashboardInfo(dateSelection), AppConstants.Response.FETCHED_PURCHASE_DASHBOARD);
    }


}