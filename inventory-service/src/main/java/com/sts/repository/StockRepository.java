package com.sts.repository;
import java.util.Optional;
import java.util.UUID;

import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository extends JpaRepository<Stock, UUID>, JpaSpecificationExecutor<Stock> {

    boolean existsByName(String name);
}
