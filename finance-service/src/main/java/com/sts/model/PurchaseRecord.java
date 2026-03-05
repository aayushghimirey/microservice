package com.sts.model;

import com.sts.domain.Audit;
import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class PurchaseRecord extends Audit {

    @Column(name = "purchaseId", nullable = false, updatable = false)
    private UUID purchaseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_type", nullable = false, updatable = false)
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "money_transaction", nullable = false, updatable = false)
    private MoneyTransaction moneyTransaction;

    private BigDecimal vatAmount;

    @Column(name = "gross_total", nullable = false, updatable = false)
    private BigDecimal grossTotal;

}
