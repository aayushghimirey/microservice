package com.sts.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.model.purchase.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    boolean existsByInvoiceNumber(String invoiceNumber);
}