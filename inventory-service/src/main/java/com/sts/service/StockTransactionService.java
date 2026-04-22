package com.sts.service;

import com.sts.dto.response.StockTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface StockTransactionService {

    Page<StockTransactionResponse> getAllTransaction(Pageable pageable);

    Page<StockTransactionResponse> getAllTransactionByVariantId(UUID variantId, Pageable pageable);
}
