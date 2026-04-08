package com.sts.model.stock;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sts.domain.Audit;
import com.sts.utils.enums.TransactionReference;

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

import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stock_transaction")
@RowLevelSecurity
@RlsRule(table = "stock_transaction", policy = "stock_transaction_tenant_policy", requiredVariable = "app.tenant_id")
public class StockTransaction extends Audit {

    @Column(name = "reference_id", updatable = false)
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", updatable = false)
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
