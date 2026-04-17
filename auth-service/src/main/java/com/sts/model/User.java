package com.sts.model;


import com.sts.utils.UserRole;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
//@RowLevelSecurity
//@RlsRule(policy = "user_isolation_policy", requiredVariable = "tenant_id", table = "users")
public class User implements UserDetails {

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


    @Column(nullable = false, length = 250)
    private String username;

    @Column(unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", unique = true, length = 150)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    private String permission;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }


}
