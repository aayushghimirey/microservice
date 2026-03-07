package com.sts.repository;

import com.sts.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Invoice findBySessionId(UUID sessionId);
}
