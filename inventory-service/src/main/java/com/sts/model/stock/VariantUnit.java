package com.sts.model.stock;

import java.math.BigDecimal;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sts.domain.Audit;
import com.sts.utils.enums.UnitType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "variant_unit")
@RowLevelSecurity
@RlsRule(table = "variant_unit", policy = "variant_unit_tenant_policy", requiredVariable = "app.tenant_id")
public class VariantUnit extends Audit {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "conversion_rate", nullable = false)
    private BigDecimal conversionRate;

    @Column(name = "unit_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_variant_id", nullable = false)
    private StockVariant stockVariant;
}
