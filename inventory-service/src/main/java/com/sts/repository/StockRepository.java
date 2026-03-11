package com.sts.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sts.model.stock.Stock;

public interface StockRepository extends JpaRepository<Stock, UUID>, JpaSpecificationExecutor<Stock> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
