package com.sts.model;


import com.sts.domain.Audit;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class InvoiceItem extends Audit {

    private UUID menuItemId;
    private double quantity;

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
