package com.sts.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.model.stock.VariantUnit;

public interface VariantUnitRepository extends JpaRepository<VariantUnit, UUID> {

    Optional<VariantUnit> findByIdAndStockVariantId(UUID id, UUID stockVariantId);
}
