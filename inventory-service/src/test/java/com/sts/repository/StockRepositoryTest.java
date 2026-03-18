package com.sts.repository;

import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.utils.enums.StockType;
import com.sts.utils.enums.UnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void setup() {
        stockRepository.deleteAll();
    }

    public Stock buildStock() {
        VariantUnit unit = VariantUnit.builder()
                .name("caret")
                .conversionRate(BigDecimal.valueOf(24))
                .unitType(UnitType.BOTH)
                .build();

        StockVariant variant = StockVariant.builder()
                .name("coke 100 ml")
                .baseUnit("pic")
                .build();

        variant.addUnit(unit);

        Stock stock = Stock.builder()
                .name("coke")
                .type(StockType.BEVERAGE)
                .build();

        stock.addVariant(variant);
        return stock;
    }

    @Test
    void existsByName() {

        // Arrange
        stockRepository.save(buildStock());

        // Act
        boolean coke = stockRepository.existsByName("coke");

        // Assert
        assertTrue(coke);
    }
}