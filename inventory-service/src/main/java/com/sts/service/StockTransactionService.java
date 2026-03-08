package com.sts.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sts.dto.response.StockTransactionResponse;


public interface StockTransactionService {

    Page<StockTransactionResponse> getAllTransaction(Pageable pageable);

    Page<StockTransactionResponse> getAllTransactionByVariantId(UUID variantId, Pageable pageable);
}
