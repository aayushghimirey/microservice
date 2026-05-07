package com.sts.model;


import com.sts.domain.Audit;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "staff")

@RowLevelSecurity
@RlsRule(table = "staff", requiredVariable = "app.tenant_id", policy = "staff_tenant_policy")
public class Staff extends Audit {

    private String name;
    private String address;
    private String role;
    private String contactNumber;
    private String[] permissions;

}
