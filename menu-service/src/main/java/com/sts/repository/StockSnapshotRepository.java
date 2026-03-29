package com.sts.repository;

import com.sts.model.StockSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockSnapshotRepository extends JpaRepository<StockSnapshot, UUID> {

    boolean existsByStockId(UUID stockId);

}
