package com.sts.controller;

import java.util.List;
import java.util.UUID;

import com.sts.utils.constant.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.response.StockTransactionResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.StockTransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(AppConstants.STOCK_BASE_PATH)
@RequiredArgsConstructor
public class StockTransactionController {

    private final StockTransactionService stockTransactionService;

    @GetMapping("/transactions")
    public ResponseEntity<PagedResponse<List<StockTransactionResponse>>> getAllStockTransactions(
            PageRequestDto pageRequestDto) {

        var transactions = stockTransactionService.getAllTransaction(pageRequestDto.buildPageable());

        return AppResponse.success(transactions, AppConstants.Response.FETCHED_TRANSACTION);
    }

    @GetMapping("/transactions/{variantId}")
    public ResponseEntity<PagedResponse<List<StockTransactionResponse>>> getStockTransactionsByVariant(
            @PathVariable UUID variantId,
            PageRequestDto pageRequestDto) {

        var transactions = stockTransactionService.getAllTransactionByVariantId(
                variantId,
                pageRequestDto.buildPageable());

        return AppResponse.success(transactions, AppConstants.Response.FETCHED_TRANSACTION);
    }
}
