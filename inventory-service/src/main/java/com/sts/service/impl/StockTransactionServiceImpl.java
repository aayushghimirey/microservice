package com.sts.service.impl;

import com.sts.dto.response.StockTransactionResponse;
import com.sts.filter.TenantHolder;
import com.sts.repository.StockTransactionRepository;
import com.sts.service.StockTransactionService;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockTransactionServiceImpl implements StockTransactionService {

    private final StockTransactionRepository stockTransactionRepository;

    private final RlsContext rlsContext;

    /*
     * Queries
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockTransactionResponse> getAllTransaction(Pageable pageable) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return stockTransactionRepository.findAllTransactions(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockTransactionResponse> getAllTransactionByVariantId(UUID variantId, Pageable pageable) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return stockTransactionRepository.findAllTransactionsByVariantId(variantId, pageable);
    }

}
