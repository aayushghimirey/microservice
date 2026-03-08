package com.sts.model.stock;

import java.math.BigDecimal;
import java.util.UUID;

import com.sts.constant.enums.TransactionReference;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sts.domain.Audit;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stock_transaction")
public class StockTransaction extends Audit {

    @Column(name = "reference_id", updatable = false)
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false, updatable = false)
    private TransactionReference referenceType;

    @Column(name = "variant_id", nullable = false, updatable = false)
    private UUID variantId;

    @Column(name = "unit_id", nullable = false, updatable = false)
    private UUID unitId;

    @Column(name = "quantity_change", nullable = false, updatable = false)
    private BigDecimal quantityChange;

    @Column(name = "balance_after", nullable = false, updatable = false)
    private BigDecimal balanceAfter;

    @Column(name = "remark")
    private String remark;

}
