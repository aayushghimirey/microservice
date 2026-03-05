package com.sts.repository;
import com.sts.model.stock.StockTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, UUID> {

    Page<StockTransaction> findAllByVariantId(UUID variantId, Pageable pageable);
}
