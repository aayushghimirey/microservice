package com.sts.repository;

import com.sts.dto.response.ReservationOrderInfo;
import com.sts.model.Reservation;
import com.sts.utils.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Reservation findBySessionId(UUID sessionId);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);


    @Query("""
                SELECT r.status, COUNT(r)
                FROM Reservation r
                WHERE r.createdDateTime >= :from
                GROUP BY r.status
            """)
    List<Object[]> countByStatusFrom(@Param("from") Instant from);
}
