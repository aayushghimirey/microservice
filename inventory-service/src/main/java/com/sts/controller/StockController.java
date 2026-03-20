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

                log.info(AppConstants.REQUEST_MESSAGES.START_CREATE_STOCK);

                ResponseEntity<ApiResponse<StockResponse>> response = AppResponse.success(
                                stockService.createStock(command),
                                AppConstants.SUCCESS_MESSAGES.STOCK_CREATED);

                log.info(AppConstants.REQUEST_MESSAGES.END_CREATE_STOCK);
                return response;
        }

        @PostMapping("/{stockId}")
        public ResponseEntity<ApiResponse<StockResponse>> updateStock(
                        @PathVariable UUID stockId,
                        @Valid @RequestBody UpdateStockCommand command) {

                log.info(AppConstants.REQUEST_MESSAGES.START_UPDATE_STOCK);

                ResponseEntity<ApiResponse<StockResponse>> response = AppResponse.success(
                                stockService.updateStock(stockId, command),
                                AppConstants.SUCCESS_MESSAGES.STOCK_UPDATED);

                log.info(AppConstants.REQUEST_MESSAGES.END_UPDATE_STOCK);
                return response;
        }

        @PostMapping("/adjustments")
        public ResponseEntity<ApiResponse<Void>> adjustStock(
                        @Valid @RequestBody StockAdjustmentCommand command) {

                log.info(AppConstants.REQUEST_MESSAGES.START_ADJUST_STOCK);

                stockService.adjustStock(command);

                log.info(AppConstants.REQUEST_MESSAGES.END_ADJUST_STOCK);
                return AppResponse.noContent();
        }

        @GetMapping
        public ResponseEntity<PagedResponse<List<StockResponse>>> getStocks(
                        @ModelAttribute PageRequestDto pageRequestDto,
                        @Valid @ModelAttribute GetStockQueryRequest queryRequest) {

                log.info(AppConstants.REQUEST_MESSAGES.START_FETCH_STOCK);

                ResponseEntity<PagedResponse<List<StockResponse>>> response = AppResponse.success(
                                stockService.getAllQueryStock(queryRequest, pageRequestDto.buildPageable()),
                                AppConstants.SUCCESS_MESSAGES.STOCKS_FETCHED);

                log.info(AppConstants.REQUEST_MESSAGES.END_FETCH_STOCK);
                return response;
        }

        @GetMapping("/{stockId}/variants")
        public ResponseEntity<PagedResponse<List<StockResponse.VariantResponse>>> getStockVariants(
                        @PathVariable UUID stockId,
                        @ModelAttribute PageRequestDto pageRequestDto) {

                log.info(AppConstants.REQUEST_MESSAGES.START_FETCH_STOCK_VARIANTS);

                ResponseEntity<PagedResponse<List<StockResponse.VariantResponse>>> response = AppResponse.success(
                                stockService.getAllVariantByStockId(stockId, pageRequestDto.buildPageable()),
                                AppConstants.SUCCESS_MESSAGES.VARIANTS_FETCHED);

                log.info(AppConstants.REQUEST_MESSAGES.END_FETCH_STOCK_VARIANTS);
                return response;
        }

        @GetMapping("/variants/{variantId}/units/{unitId}/exists")
        public ResponseEntity<ApiResponse<Boolean>> existsVariantUnit(
                        @PathVariable UUID variantId,
                        @PathVariable UUID unitId) {

                log.info(AppConstants.REQUEST_MESSAGES.START_VERIFY_VARIANT);

                ResponseEntity<ApiResponse<Boolean>> response = AppResponse.success(
                                stockService.existsByVariantIdAndUnitId(variantId, unitId),
                                AppConstants.SUCCESS_MESSAGES.STOCK_VERIFIED);

                log.info(AppConstants.REQUEST_MESSAGES.END_VERIFY_VARIANT);
                return response;
        }
}