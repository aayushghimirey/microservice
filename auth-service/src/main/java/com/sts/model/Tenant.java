package com.sts.model;

import com.sts.domain.Audit;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tenant")
@EntityListeners(AuditingEntityListener.class)

@RowLevelSecurity
@RlsRule(table = "tenant", policy = "tenant_rls_policy", requiredVariable = "id")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 250)
    private String name;

    @Column(name = "contact_email", length = 150)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
