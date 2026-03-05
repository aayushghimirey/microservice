package com.sts.repository;

import com.sts.model.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, UUID> {
}
