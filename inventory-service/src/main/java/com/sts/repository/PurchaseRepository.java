package com.sts.repository;

import com.sts.model.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface PurchaseRepository
        extends JpaRepository<Purchase, UUID>,
        JpaSpecificationExecutor<Purchase> {

    boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("""
                SELECT SUM(p.grossTotal)
                FROM Purchase p
                WHERE p.createdDateTime >= :from
            """)
    BigDecimal findPurchaseAmountSum(@Param("from") Instant from);

    @Query("""
                SELECT COUNT(p)
                FROM Purchase p
                WHERE p.createdDateTime >= :from
            """)
    Long countPurchase(@Param("from") Instant from);
}