package com.sts.model.stock;

import com.sts.domain.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EntityListeners.class)
@Table(name = "stock_variant")
public class StockVariant extends Audit {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "base_unit", nullable = false)
    private String baseUnit;

    @Column(name = "opening_stock", nullable = false)
    @Builder.Default
    private BigDecimal openingStock = BigDecimal.ZERO;

    @Column(name = "current_stock", nullable = false)
    @Builder.Default
    private BigDecimal currentStock = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;


    @OneToMany(mappedBy = "stockVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VariantUnit> units = new ArrayList<>();

    public void setOpeningStock(BigDecimal openingStock) {
        if (openingStock != null && openingStock.compareTo(BigDecimal.ZERO) > 0) {
            this.openingStock = openingStock;

            if (this.currentStock == null || this.currentStock.compareTo(BigDecimal.ZERO) <= 0) {
                this.currentStock = this.openingStock;
            }
        }
    }

    public void addUnit(VariantUnit unit) {
        if (unit == null) return;
        units.add(unit);
        unit.setStockVariant(this);
    }

}
