package com.sts.service.impl;

import com.sts.dto.response.StockTransactionResponse;
import com.sts.repository.StockTransactionRepository;
import com.sts.service.StockTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@AllArgsConstructor
public class StockTransactionServiceImpl implements StockTransactionService {

    private final StockTransactionRepository stockTransactionRepository;

    @Transactional(readOnly = true)
    public Page<StockTransactionResponse> getAllTransaction(Pageable pageable) {
        return stockTransactionRepository.findAllTransactions(pageable);
    }

    @Transactional(readOnly = true)
    public Page<StockTransactionResponse> getAllTransactionByVariantId(UUID variantId, Pageable pageable) {
        return stockTransactionRepository.findAllTransactionsByVariantId(variantId, pageable);
    }

}
