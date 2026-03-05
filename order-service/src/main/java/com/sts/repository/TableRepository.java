package com.sts.repository;

import com.sts.domain.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TableRepository extends JpaRepository<Table, UUID> {
}
