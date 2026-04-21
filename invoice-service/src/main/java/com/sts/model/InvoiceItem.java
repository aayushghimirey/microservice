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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@RowLevelSecurity
@RlsRule(table = "invoice_item", policy = "invoice_item_tenant_policy", requiredVariable = "app.tenant_id")
public class InvoiceItem extends Audit {

    @Column(nullable = false)
    private String name;
    private UUID menuItemId;
    private double quantity;
    @Column(nullable = false)
    private BigDecimal price;

    @Builder.Default
    private boolean printable = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Builder.Default
    @OneToMany(mappedBy = "invoiceItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItemIngredient> ingredients = new ArrayList<>();  // ✅ added

    public void addIngredient(InvoiceItemIngredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setInvoiceItem(this);
    }
}
