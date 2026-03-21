package com.sts.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.StockService;
import com.sts.utils.constant.AppConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(AppConstants.STOCK_BASE_PATH)
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<ApiResponse<StockResponse>> createStock(
            @Valid @RequestBody CreateStockCommand command) {

        log.info(AppConstants.Request.CREATE_STOCK_START, command.type());

        StockResponse stock = stockService.createStock(command);

        log.info(AppConstants.Request.CREATE_STOCK_END, stock.id());

        return AppResponse.success(stock, AppConstants.Response.STOCK_CREATED);
    }

    @PostMapping("/{stockId}")
    public ResponseEntity<ApiResponse<StockResponse>> updateStock(
            @PathVariable UUID stockId,
            @Valid @RequestBody UpdateStockCommand command) {

        log.info(AppConstants.Request.UPDATE_STOCK_START, stockId);

        StockResponse stock = stockService.updateStock(stockId, command);

        log.info(AppConstants.Request.UPDATE_STOCK_END, stock.id());

        return AppResponse.success(stock, AppConstants.Response.STOCK_UPDATED);
    }

    @PostMapping("/adjustments")
    public ResponseEntity<ApiResponse<Void>> adjustStock(
            @Valid @RequestBody StockAdjustmentCommand command) {

        log.info(AppConstants.Request.CREATE_ADJUSTMENT_START, command.variantId());

        stockService.adjustStock(command);

        log.info(AppConstants.Request.CREATE_ADJUSTMENT_END, command.variantId());
        return AppResponse.noContent();
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<StockResponse>>> getStocks(
            @ModelAttribute PageRequestDto pageRequestDto,
            @Valid @ModelAttribute GetStockQueryRequest queryRequest) {


        return AppResponse.success(
                stockService.getAllQueryStock(queryRequest, pageRequestDto.buildPageable()),
                AppConstants.Response.FETCHED_STOCKS);

    }

    @GetMapping("/{stockId}/variants")
    public ResponseEntity<PagedResponse<List<StockResponse.VariantResponse>>> getStockVariants(
            @PathVariable UUID stockId,
            @ModelAttribute PageRequestDto pageRequestDto) {


        return AppResponse.success(
                stockService.getAllVariantByStockId(stockId, pageRequestDto.buildPageable()),
                AppConstants.Response.FETCHED_STOCK_VARIANTS);

    }

    @GetMapping("/variants/{variantId}/units/{unitId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsVariantUnit(
            @PathVariable UUID variantId,
            @PathVariable UUID unitId) {

        log.info(AppConstants.Request.START_VERIFY_VARIANT, variantId, unitId);

        return AppResponse.success(
                stockService.existsByVariantIdAndUnitId(variantId, unitId),
                AppConstants.Response.STOCK_VERIFIED);

    }
}