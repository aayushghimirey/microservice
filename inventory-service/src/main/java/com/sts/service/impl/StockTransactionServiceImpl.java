package com.sts.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.response.StockTransactionResponse;
import com.sts.repository.StockTransactionRepository;
import com.sts.service.StockTransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockTransactionServiceImpl implements StockTransactionService {

    private final StockTransactionRepository stockTransactionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<StockTransactionResponse> getAllTransaction(Pageable pageable) {
        return stockTransactionRepository.findAllTransactions(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockTransactionResponse> getAllTransactionByVariantId(UUID variantId, Pageable pageable) {
        return stockTransactionRepository.findAllTransactionsByVariantId(variantId, pageable);
    }

}
