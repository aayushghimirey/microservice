package com.sts.model;

import com.sts.domain.Audit;
import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "purchase_record")

@RowLevelSecurity
@RlsRule(table = "purchase_record", policy = "purchase_record_tenant_policy", requiredVariable = "app.tenant_id")
public class PurchaseRecord extends Audit {

    @Column(name = "purchase_id", nullable = false, updatable = false, unique = true)
    private UUID purchaseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_type", nullable = false, updatable = false)
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "money_transaction", nullable = false, updatable = false)
    private MoneyTransaction moneyTransaction;

    @Column(name = "vat_amount")
    private BigDecimal vatAmount;

    @Column(name = "gross_total", nullable = false, updatable = false)
    private BigDecimal grossTotal;

    private Instant purchaseDateTime;

}
