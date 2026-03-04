package com.sts.stock.domain.repository;

import com.sts.stock.domain.model.VariantUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VariantUnitRepository extends JpaRepository<VariantUnit, UUID> {
}
