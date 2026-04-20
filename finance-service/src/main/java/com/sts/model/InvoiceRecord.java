package com.sts.model;


import com.sts.domain.Audit;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "invoice_record")
@RowLevelSecurity
@RlsRule(table = "invoice_record", policy = "invoice_record_tenant_policy", requiredVariable = "app.tenant_id")
public class InvoiceRecord extends Audit {

    @Column(name = "invoice_id", nullable = false, updatable = false, unique = true)
    private UUID invoiceId;

    @Builder.Default
    @Column(name = "gross_total", nullable = false)
    private BigDecimal grossTotal = BigDecimal.ZERO;

    @Column(name = "reservation_start_time", updatable = false, nullable = false)
    private Instant reservationTime;

    @Column(name = "reservation_end_time", updatable = false, nullable = false)
    private Instant reservationEndTime;


}
