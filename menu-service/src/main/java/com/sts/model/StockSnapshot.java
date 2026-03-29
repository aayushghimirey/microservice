package com.sts.model;

import com.sts.domain.Audit;
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

