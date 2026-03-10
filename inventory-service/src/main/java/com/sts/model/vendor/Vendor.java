package com.sts.model.vendor;

import com.sts.domain.Audit;
import com.sts.model.purchase.Purchase;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Vendor extends Audit {

    private String name;
    private String address;
    private String contactNumber;
    private String panNumber;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    public List<Purchase> purchases = new ArrayList<>();

}
