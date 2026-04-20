package com.sts.model;


import com.sts.domain.Audit;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "business_detail")

@RowLevelSecurity
@RlsRule(table = "business_detail", policy = "business_detail_isolation", requiredVariable = "tenant_id")
public class BusinessDetail extends Audit {

    @Column(name = "company_name", nullable = false)
    private String companyName;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "pan_number", nullable = false)
    private String panNumber;
    @Column(name = "business_number", nullable = false)
    private String businessNumber;
    @Column(name = "business_email", nullable = false)
    private String businessEmail;


}
