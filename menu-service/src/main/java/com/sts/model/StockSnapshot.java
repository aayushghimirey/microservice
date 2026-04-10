package com.sts.model;

import com.sts.domain.Audit;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@RowLevelSecurity
@RlsRule(table = "stock_snapshot", requiredVariable = "app.tenant_id", policy = "stock_snapshot_tenant_policy")
public class StockSnapshot extends Audit {

    @Column(name = "stock_id")
    private UUID stockId;

    @OneToMany(mappedBy = "stockSnapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariantSnapshot> variants = new ArrayList<>();

    public void addVariant(VariantSnapshot variant) {
        if (variant == null) return;
        this.variants.add(variant);
        variant.setStockSnapshot(this);
    }

}

