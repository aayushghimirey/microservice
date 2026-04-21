package com.sts.repository;

import com.sts.model.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, UUID> {

    Optional<PurchaseRecord> findByPurchaseId(UUID purchaseId);

    @Query("""
                SELECT SUM(p.grossTotal)
                FROM PurchaseRecord p
                WHERE p.createdDateTime >= :from
            """)
    BigDecimal sumGrossTotal(@Param("from") Instant from);

    @Query("""
                SELECT SUM(p.vatAmount)
                FROM PurchaseRecord p
                WHERE p.createdDateTime >= :from
            """)
    BigDecimal sumVatAmountTotal(@Param("from") Instant from);
}