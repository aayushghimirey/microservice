package com.sts.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

        log.info("Request received: createStock stockType={}", command.type());

        StockResponse stock = stockService.createStock(command);

        log.info("Request completed: createStock stockId={}", stock.id());

        return AppResponse.success(stock, AppConstants.Response.STOCK_CREATED);
    }

    @PatchMapping("/{stockId}")
    public ResponseEntity<ApiResponse<StockResponse>> updateStock(
            @PathVariable UUID stockId,
            @RequestBody UpdateStockCommand command) {

        log.info("Request received: updateStock stockId={}", stockId);

        StockResponse stock = stockService.updateStock(stockId, command);

        log.info("Request completed: updateStock stockId={}", stock.id());

        return AppResponse.success(stock, AppConstants.Response.STOCK_UPDATED);
    }

    @PostMapping("/adjustments")
    public ResponseEntity<ApiResponse<Void>> adjustStock(
            @Valid @RequestBody StockAdjustmentCommand command) {

        log.info("Request received: adjustStock variantId={}", command.variantId());

        stockService.adjustStock(command);

        log.info("Request completed: adjustStock variantId={}", command.variantId());

        return AppResponse.noContent();
    }

    /* Query requests */

    @GetMapping
    public ResponseEntity<PagedResponse<List<StockResponse>>> getStocks(
            @ModelAttribute PageRequestDto pageRequestDto,
            @Valid @ModelAttribute GetStockQueryRequest queryRequest) {

        log.info("Request received: getStocks page={} size={}",
                pageRequestDto.getPage(), pageRequestDto.getSize());

        var response = stockService.getAllQueryStock(queryRequest, pageRequestDto.buildPageable());

        log.info("Request completed: getStocks totalElements={}", response.getTotalElements());

        return AppResponse.success(response, AppConstants.Response.FETCHED_STOCKS);
    }

    @GetMapping("/{stockId}/variants")
    public ResponseEntity<PagedResponse<List<StockResponse.VariantResponse>>> getStockVariants(
            @PathVariable UUID stockId,
            @ModelAttribute PageRequestDto pageRequestDto) {

        log.info("Request received: getStockVariants stockId={}", stockId);

        var response = stockService.getAllVariantByStockId(stockId, pageRequestDto.buildPageable());

        log.info("Request completed: getStockVariants stockId={} totalElements={}",
                stockId, response.getTotalElements());

        return AppResponse.success(response, AppConstants.Response.FETCHED_STOCK_VARIANTS);
    }

    @GetMapping("/variants/{variantId}/units/{unitId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsVariantUnit(@PathVariable UUID variantId,
                                                                  @PathVariable UUID unitId) {

        log.info("Request received: existsVariantUnit variantId={} unitId={}", variantId, unitId);

        var exists = stockService.existsByVariantIdAndUnitId(variantId, unitId);

        log.info("Request completed: existsVariantUnit variantId={} unitId={} exists={}",
                variantId, unitId, exists);

        return AppResponse.success(exists, AppConstants.Response.STOCK_VERIFIED);
    }
}