package com.sts.model;

import com.sts.domain.Audit;
import com.sts.utils.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RlsRule;
import io.github.aayushghimirey.jpa_postgres_rls.annotation.RowLevelSecurity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@RowLevelSecurity
@RlsRule(table = "tables", requiredVariable = "app.tenant_id", policy = "tables_tenant_policy")
@jakarta.persistence.Table(name = "tables")
public class Table extends Audit {

    @Column(name = "name", nullable = false)
    private String name;
    private int capacity;
    private String location;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    @OneToMany(mappedBy = "table", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        if (reservation == null) return;
        reservations.add(reservation);
        reservation.setTable(this);
    }
}
