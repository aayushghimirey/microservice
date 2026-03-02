package com.sts.stock.domain.model;

import com.sts.domain.Audit;
import com.sts.stock.domain.enums.StockType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EntityListeners.class)
@Table(name = "stock")
public class Stock extends Audit {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StockType type;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StockVariant> variants = new ArrayList<>();

    public void addVariant(StockVariant variant) {
        if (variant == null) return;
        variants.add(variant);
        variant.setStock(this);
    }

}
