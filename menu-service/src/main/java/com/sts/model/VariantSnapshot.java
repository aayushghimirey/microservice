package com.sts.model;

import com.sts.domain.Audit;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class VariantSnapshot extends Audit {

    @Column(name = "variant_id")
    private UUID variantId;

    Set<UUID> unitIds;

    @ManyToOne
    @JoinColumn(name = "stock_snapshot_id")
    private StockSnapshot stockSnapshot;


}
