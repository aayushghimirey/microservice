package com.sts.repository;

import com.sts.model.Reservation;
import com.sts.utils.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Reservation findBySessionId(UUID sessionId);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

}
