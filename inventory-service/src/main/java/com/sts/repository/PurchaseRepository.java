package com.sts.repository;

import com.sts.model.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    boolean existsByInvoiceNumber(String invoiceNumber);
}