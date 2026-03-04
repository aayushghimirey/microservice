package com.sts.stock.domain.model;

import com.sts.domain.Audit;
import com.sts.stock.domain.enums.UnitType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EntityListeners.class)
@Table(name = "variant_unit")
public class VariantUnit extends Audit {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "conversion_rate", nullable = false)
    private BigDecimal conversionRate;

    @Column(name = "unit_type", nullable = false)
    private UnitType unitType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_variant_id", nullable = false)
    private StockVariant stockVariant;
}
