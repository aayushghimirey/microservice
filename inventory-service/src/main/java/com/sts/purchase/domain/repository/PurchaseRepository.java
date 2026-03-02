package com.sts.purchase.domain.repository;

import com.sts.purchase.domain.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
}
