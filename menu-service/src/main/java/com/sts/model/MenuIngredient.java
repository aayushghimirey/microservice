package com.sts.model;

import com.sts.domain.Audit;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

/**
 * JPA Entity representing a single ingredient on a {@link Menu} item.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "menu_ingredient")
@EntityListeners(AuditingEntityListener.class)
@RowLevelSecurity
@RlsRule(table = "menu_ingredient", requiredVariable = "app.tenant_id", policy = "menu_ingredient_tenant_policy")
public class MenuIngredient extends Audit {

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(name = "unit_id", nullable = false)
    private UUID unitId;

    @Column(name = "quantity", nullable = false)
    private double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
}
