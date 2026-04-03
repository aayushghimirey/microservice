package com.sts.repository;

import com.sts.model.InvoiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRecordRepository extends JpaRepository<InvoiceRecord, UUID> {

    Optional<InvoiceRecord> findByInvoiceId(UUID invoiceId);

}
