package com.sts.repository;

import com.sts.model.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    boolean findByInvoiceNumber(String invoiceNumber);

    boolean exitsByInvoiceNumber(String invoiceNumber);
}
