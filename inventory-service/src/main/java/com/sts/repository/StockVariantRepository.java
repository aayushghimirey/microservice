package com.sts.repository;

import java.util.UUID;

import com.sts.model.stock.StockVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockVariantRepository
        extends JpaRepository<StockVariant, UUID>, JpaSpecificationExecutor<StockVariant> {
    Page<StockVariant> findAllByStockId(UUID stockId, Pageable pageable);


 }
