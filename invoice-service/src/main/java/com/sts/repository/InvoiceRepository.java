package com.sts.repository;

import com.sts.utils.enums.InvoiceStatus;
import com.sts.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Invoice findBySessionId(UUID sessionId);

    List<Invoice> findByStatus(InvoiceStatus invoiceStatus);
}
