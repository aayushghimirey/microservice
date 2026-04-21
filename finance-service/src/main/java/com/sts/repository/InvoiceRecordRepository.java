package com.sts.repository;

import com.sts.model.InvoiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRecordRepository extends JpaRepository<InvoiceRecord, UUID> {

    Optional<InvoiceRecord> findByInvoiceId(UUID invoiceId);

    @Query("""
        SELECT SUM(i.grossTotal)
        FROM InvoiceRecord i
        WHERE i.createdDateTime >= :from
    """)
    BigDecimal sumGrossTotal(@Param("from") Instant from);
}