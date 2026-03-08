package com.sts.repository;

import com.sts.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TableRepository extends JpaRepository<Table, UUID> {

    boolean existsByName(String name);

}
