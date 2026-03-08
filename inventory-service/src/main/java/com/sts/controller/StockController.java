package com.sts.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.dto.response.StockTransactionResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.StockService;
import com.sts.service.StockTransactionService;
import com.sts.utils.contant.AppConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
POST   /stocks  (create-stock)
POST   /stocks/{stockId}  (update-stock)
POST   /stocks/adjustments (create-adjustment)

GET    /stocks (get-all-stocks)
GET    /stocks/search (search-stocks)
GET    /stocks/{stockId}/variants (get-stock-variants)

GET    /stocks/variants/{variantId}/units/{unitId}/exists (validate-stock-id-and-unit-id)

GET    /stocks/transactions (get-all-stock-transactions)
GET    /stocks/transactions/{variantId} (get-stock-transactions-by-variant)
*/

@Slf4j
@Validated
@RestController
@RequestMapping(AppConstants.STOCK_BASE_PATH)
@AllArgsConstructor
public class StockController {

    private final StockService stockService;
    private final StockTransactionService stockTransactionService;

    /*
     * Commands
     */

    @PostMapping
    public ResponseEntity<ApiResponse<StockResponse>> createStock(
            @Valid @RequestBody CreateStockCommand command) {

        log.info(AppConstants.LOG_MESSAGES.CREATING_STOCK, command.name());

        var stock = stockService.createStock(command);

        return AppResponse.success(stock, AppConstants.SUCCESS_MESSAGES.STOCK_CREATED);
    }

    @PostMapping("/{stockId}")
    public ResponseEntity<ApiResponse<StockResponse>> updateStock(
            @PathVariable UUID stockId,
            @Valid @RequestBody UpdateStockCommand command) {

        log.info(AppConstants.LOG_MESSAGES.UPDATING_STOCK, stockId);

        var stock = stockService.updateStock(stockId, command);

        return AppResponse.success(stock, AppConstants.SUCCESS_MESSAGES.STOCK_UPDATED);
    }

    @PostMapping("/adjustments")
    public ResponseEntity<ApiResponse<Void>> createAdjustment(
            @Valid @RequestBody StockAdjustmentCommand command) {

        log.info(AppConstants.LOG_MESSAGES.CREATING_ADJUSTMENT,
                command.variantId(),
                command.unitId(),
                command.quantity());

        stockService.adjustStock(command);

        return AppResponse.noContent();
    }

    /*
     * Queries
     */

    @GetMapping
    public ResponseEntity<PagedResponse<List<StockResponse>>> getAllStocks(
            PageRequestDto pageRequestDto) {

        var stocks = stockService.getAllStock(pageRequestDto.buildPageable());

        return AppResponse.success(stocks, AppConstants.SUCCESS_MESSAGES.STOCKS_FETCHED);
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<List<StockResponse>>> searchStocks(
            PageRequestDto pageRequestDto,
            GetStockQueryRequest queryRequest) {

        var stocks = stockService.getAllQueryStock(queryRequest, pageRequestDto.buildPageable());

        return AppResponse.success(stocks, AppConstants.SUCCESS_MESSAGES.STOCKS_FETCHED);
    }

    @GetMapping("/{stockId}/variants")
    public ResponseEntity<PagedResponse<List<StockResponse.VariantResponse>>> getStockVariants(
            @PathVariable UUID stockId,
            PageRequestDto pageRequestDto) {

        var variants = stockService.getAllVariantByStockId(stockId, pageRequestDto.buildPageable());

        return AppResponse.success(variants, AppConstants.SUCCESS_MESSAGES.VARIANTS_FETCHED);
    }

    /*
     * External validation API (used by menu service)
     */

    @GetMapping("/variants/{variantId}/units/{unitId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> validateStock(
            @PathVariable UUID variantId,
            @PathVariable UUID unitId) {

        log.info(AppConstants.LOG_MESSAGES.VALIDATING_VARIANT, variantId, unitId);

        var exists = stockService.validateVariantIdWithUnitId(variantId, unitId);

        return AppResponse.success(exists, AppConstants.SUCCESS_MESSAGES.VARIANTS_FETCHED);
    }

    /*
     * Transactions
     */

    @GetMapping("/transactions")
    public ResponseEntity<PagedResponse<List<StockTransactionResponse>>> getAllStockTransactions(
            PageRequestDto pageRequestDto) {

        var transactions = stockTransactionService.getAllTransaction(pageRequestDto.buildPageable());

        return AppResponse.success(transactions, AppConstants.SUCCESS_MESSAGES.TRANSACTIONS_FETCHED);
    }

    @GetMapping("/transactions/{variantId}")
    public ResponseEntity<PagedResponse<List<StockTransactionResponse>>> getStockTransactionsByVariant(
            @PathVariable UUID variantId,
            PageRequestDto pageRequestDto) {

        var transactions = stockTransactionService.getAllTransactionByVariantId(
                variantId,
                pageRequestDto.buildPageable());

        return AppResponse.success(transactions, AppConstants.SUCCESS_MESSAGES.TRANSACTIONS_FETCHED);
    }
}