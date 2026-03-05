package com.sts.domain.model;

import com.sts.domain.Audit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.*;
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
@jakarta.persistence.Table(name = "tables")
public class Table extends Audit {

    @Column(name = "name", nullable = false)
    private String name;
    private int capacity;
    private String location;

    @OneToMany(mappedBy = "table", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        if (reservation == null) return;
        reservations.add(reservation);
        reservation.setTable(this);
    }
}
