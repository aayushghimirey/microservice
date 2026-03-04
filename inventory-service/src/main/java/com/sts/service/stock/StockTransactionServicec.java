package com.sts.stock.application.usecase;


import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockTransactionRepository;
import com.sts.dto.StockTransactionResponse;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class StockTransactionUseCase {

    private final StockTransactionRepository stockTransactionRepository;
    private final EntityManager entityManager;

    public Page<StockTransactionResponse> getAllTransaction(Pageable pageable) {
        return stockTransactionRepository.findAll(pageable).map(tx -> {
            var variant = entityManager.find(StockVariant.class, tx.getVariantId());
            var unit = entityManager.find(VariantUnit.class, tx.getUnitId());
            return StockTransactionResponse.fromEntity(tx, variant.getName(), unit.getName());
        });
    }

    public Page<StockTransactionResponse> getAllTransactionByVariantId(UUID variantId, Pageable pageable) {
        return stockTransactionRepository.findAllByVariantId(variantId, pageable).map(tx -> {
            var variant = entityManager.find(StockVariant.class, tx.getVariantId());
            var unit = entityManager.find(VariantUnit.class, tx.getUnitId());
            return StockTransactionResponse.fromEntity(tx, variant.getName(), unit.getName());
        });
    }
}
