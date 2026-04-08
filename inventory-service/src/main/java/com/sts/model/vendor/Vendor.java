package com.sts.model.vendor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sts.domain.Audit;
import com.sts.model.purchase.Purchase;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vendor")
@EntityListeners(AuditingEntityListener.class)
@RowLevelSecurity
@RlsRule(table = "vendor", policy = "vendor_tenant_policy", requiredVariable = "app.tenant_id")
public class Vendor extends Audit {

    private String name;
    private String address;
    private String contactNumber;
    private String panNumber;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Purchase> purchases = new ArrayList<>();

}
