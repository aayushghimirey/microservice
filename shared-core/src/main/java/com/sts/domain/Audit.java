package com.sts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditListener.class)
@AllArgsConstructor
@SuperBuilder
public abstract class Audit {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(updatable = false, name = "created_date_time")
    private LocalDateTime createdDateTime;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    @LastModifiedDate
    @Column(name = "last_updated_date_time")
    private LocalDateTime lastUpdatedDateTime;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

}