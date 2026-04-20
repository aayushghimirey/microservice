package com.sts.model;

import com.sts.domain.Audit;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@RowLevelSecurity
@RlsRule(table = "variant_snapshot", requiredVariable = "app.tenant_id", policy = "variant_snapshot_tenant_policy")
public class VariantSnapshot extends Audit {

    @Column(name = "variant_id")
    private UUID variantId;

    @ElementCollection
    @CollectionTable(
            name = "variant_snapshot_unit_ids",
            joinColumns = @JoinColumn(name = "variant_snapshot_id")
    )
    @Column(name = "unit_id", nullable = false)
    private Set<UUID> unitIds;

    @ManyToOne
    @JoinColumn(name = "stock_snapshot_id")
    private StockSnapshot stockSnapshot;


}
