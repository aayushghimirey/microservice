package com.sts.entity;

import com.sts.enums.AggregateType;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class OutboxEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "aggregate_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType; // e.g., PURCHASE, STOCK

    @Column(name = "topic")
    private String topic;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxEventType eventType;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "processed", nullable = false)
    private boolean processed = false;

    @Column(nullable = false)
    private UUID tenantId;

    private LocalDateTime createdAt;
}