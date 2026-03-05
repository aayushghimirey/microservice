package com.sts.model.stock;

import com.sts.domain.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import com.sts.utils.enums.UnitType;

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
