package com.sts.repository;
import com.sts.model.stock.VariantUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VariantUnitRepository extends JpaRepository<VariantUnit, UUID> {
}
