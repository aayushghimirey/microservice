package com.sts.repository;

import com.sts.model.VariantSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VariantSnapshotRepository extends JpaRepository<VariantSnapshot, UUID> {
    Optional<VariantSnapshot> findByVariantId(UUID variantId);
}
