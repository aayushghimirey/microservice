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
@Table(name = "business_detail")

@RowLevelSecurity
@RlsRule(table = "business_detail", policy = "business_detail_isolation", requiredVariable = "tenant_id")
public class BusinessDetail extends Audit {

    private String companyName;
    private String address;
    private String panNumber;
    private String businessNumber;
    private String businessEmail;


}
