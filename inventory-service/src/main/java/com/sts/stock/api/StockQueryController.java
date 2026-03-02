package com.sts.stock.api;

import com.sts.pagination.PageRequestDto;
import com.sts.stock.application.usecase.StockTransactionUseCase;
import com.sts.stock.domain.model.StockVariant;
import com.sts.stock.dto.StockResponse;
import com.sts.stock.application.mapper.StockMapper;
import com.sts.stock.application.query.GetStockQueryRequest;
import com.sts.stock.dto.StockTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sts.stock.application.query.GetVariantQueryRequest;
import com.sts.stock.application.usecase.StockQueryUseCase;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockQueryController {

    private final StockQueryUseCase stockQueryUseCase;
    private final StockTransactionUseCase stockTransactionUseCase;
    private final StockMapper stockMapper;

    @GetMapping
    public ResponseEntity<Page<StockResponse>> getAllStocks(PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(
                stockQueryUseCase.getAllStock(pageRequestDto.buildPageable())
                        .map(stockMapper::toResponse));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<StockResponse>> getFilteredStocks(
            @ModelAttribute GetStockQueryRequest queryRequest,
            PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(
                stockQueryUseCase.getAllQueryStock(queryRequest, pageRequestDto.buildPageable())
                        .map(stockMapper::toResponse));
    }

    @GetMapping("/variant/{stockId}")
    public ResponseEntity<Page<StockResponse.VariantResponse>> getAllVariants(@PathVariable UUID stockId, PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(
                stockQueryUseCase.getAllVariantByStockId(stockId, pageRequestDto.buildPageable())
                        .map(stockMapper::toVariantResponse));
    }

    @GetMapping("/variant/{stockId}/filter")
    public ResponseEntity<Page<StockResponse.VariantResponse>> getFilteredVariants(
            @PathVariable("stockId") UUID stockId,
            @ModelAttribute GetVariantQueryRequest variantRequest,
            PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(
                stockQueryUseCase.getAllQueryVariantByStockId(stockId, variantRequest, pageRequestDto.buildPageable())
                        .map(stockMapper::toVariantResponse));
    }

    @GetMapping("/transaction")
    public ResponseEntity<Page<StockTransactionResponse>> getStockTransactions(
            PageRequestDto pageRequestDto
    ) {
        return ResponseEntity.ok(
                stockTransactionUseCase.getAllTransaction(pageRequestDto.buildPageable())
        );
    }

    @GetMapping("/transaction/{variantId}")
    public ResponseEntity<Page<StockTransactionResponse>> getStockTransactionsByVariantId(
            @PathVariable("variantId") UUID variantId,
            PageRequestDto pageRequestDto
    ) {
        return ResponseEntity.ok(
                stockTransactionUseCase.getAllTransactionByVariantId(variantId, pageRequestDto.buildPageable())
        );
    }


    // validate
    @GetMapping("/{stockId}/{variantId}/validate")
    public ResponseEntity<Boolean> validateStock(
            @PathVariable("stockId") UUID stockId,
            @PathVariable("variantId") UUID variantId
    ) {
        return ResponseEntity.ok().body(stockQueryUseCase.validateVariantIdWithUnitId(stockId, variantId));
    }

}
