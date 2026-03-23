package com.sts.domain;

import jakarta.persistence.Column;
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
@AllArgsConstructor
@SuperBuilder
public abstract class Audit {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    private UUID tenantId;

    @LastModifiedDate
    private LocalDateTime lastUpdateDateTime;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

}
