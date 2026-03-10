package com.sts.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sts.dto.response.StockTransactionResponse;
import com.sts.model.stock.StockTransaction;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, UUID> {

    @Query(value = """
            SELECT
                tx.id AS id,
                tx.reference_id AS referenceId,
                tx.reference_type AS referenceType,
                tx.variant_id AS variantId,
                sr.name AS variantName,
                tx.unit_id AS unitId,
                vu.name AS unitName,
                tx.quantity_change AS quantityChanged,
                tx.balance_after AS balanceAfter,
                tx.remark AS remark
            FROM stock_transaction tx
            JOIN stock_variant sr
                ON tx.variant_id = sr.id
            JOIN variant_unit vu
                ON tx.unit_id = vu.id
            """, countQuery = "SELECT COUNT(*) FROM stock_transaction", nativeQuery = true)
    Page<StockTransactionResponse> findAllTransactions(Pageable pageable);

    @Query(value = """
            SELECT
                tx.id AS id,
                tx.reference_id AS referenceId,
                tx.reference_type AS referenceType,
                tx.variant_id AS variantId,
                sr.name AS variantName,
                tx.unit_id AS unitId,
                vu.name AS unitName,
                tx.quantity_change AS quantityChanged,
                tx.balance_after AS balanceAfter,
                tx.remark AS remark
            FROM stock_transaction tx
            JOIN stock_variant sr
                ON tx.variant_id = sr.id
            JOIN variant_unit vu
                ON tx.unit_id = vu.id
                WHERE tx.variant_id = :variantId
            """, countQuery = "SELECT COUNT(*) FROM stock_transaction WHERE variant_id = :variantId", nativeQuery = true)
    Page<StockTransactionResponse> findAllTransactionsByVariantId(
            @Param("variantId") UUID variantId,
            Pageable pageable);
}