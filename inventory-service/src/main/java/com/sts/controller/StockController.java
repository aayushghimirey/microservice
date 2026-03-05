package com.sts.controller;


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
import com.sts.service.stock.StockService;
import com.sts.service.stock.StockTransactionService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stock")
@AllArgsConstructor
public class StockController {

    private final StockService stockService;
    private final StockTransactionService stockTransactionService;

    /*
     *  Commands
     * */
    @PostMapping
    public ResponseEntity<ApiResponse<StockResponse>> createStock(@Valid @RequestBody CreateStockCommand command) {
        var stock = stockService.createStock(command);
        return AppResponse.success(stock, "Stock created successfully");
    }

    @PostMapping("/{stockId}")
    public ResponseEntity<ApiResponse<StockResponse>> updateStock(@PathVariable("stockId") UUID stockId,
                                                                  @Valid @RequestBody UpdateStockCommand command) {
        var stock = stockService.updateStock(stockId, command);
        return AppResponse.success(stock, "Stock updated successfully");
    }

    @PostMapping("/adjustment")
    public ResponseEntity<ApiResponse<Void>> createAdjustment(@RequestBody StockAdjustmentCommand command) {
        stockService.createAdjustmentStock(command);
        return AppResponse.noContent();
    }

    /*
     * Query
     * */
    @GetMapping
    public ResponseEntity<PagedResponse<List<StockResponse>>> getAllStocks(PageRequestDto pageRequestDto) {
        var stocks = stockService.getAllStock(pageRequestDto.buildPageable());
        return AppResponse.success(stocks, "Stocks fetched successfully");
    }

    @GetMapping("/filter")
    public ResponseEntity<PagedResponse<List<StockResponse>>> getAllStocks(PageRequestDto pageRequestDto, GetStockQueryRequest queryRequest) {
        var stocks = stockService.getAllQueryStock(queryRequest, pageRequestDto.buildPageable());
        return AppResponse.success(stocks, "Stocks fetched successfully");
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<PagedResponse<List<StockResponse.VariantResponse>>> getAllVariants(
            @PathVariable("stockId") UUID stockId, PageRequestDto pageRequestDto) {
        var variants = stockService.getAllVariantByStockId(stockId, pageRequestDto.buildPageable());
        return AppResponse.success(variants, "Variants fetched successfully");
    }

    /*
     * Transactions
     * */
    @GetMapping("/transaction")
    public ResponseEntity<PagedResponse<List<StockTransactionResponse>>> getAllStockTransaction(PageRequestDto pageRequestDto) {
        var transactions = stockTransactionService.getAllTransaction(pageRequestDto.buildPageable());
        return AppResponse.success(transactions, "Transactions fetched successfully");
    }

    @GetMapping("/transaction/{variantId}")
    public ResponseEntity<PagedResponse<List<StockTransactionResponse>>> getAllStockTransactionByVariantId(
            @PathVariable("variantId") UUID variantId, PageRequestDto pageRequestDto) {
        var transactions = stockTransactionService.getAllTransactionByVariantId(variantId, pageRequestDto.buildPageable());
        return AppResponse.success(transactions, "Transactions fetched successfully");
    }

}