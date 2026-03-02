package com.sts.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreatedEvent {

    private UUID sessionId;

    private UUID reservationId;

    private String status;

    private UUID tableId;

    @Builder.Default
    private BigDecimal billAmount = BigDecimal.ZERO;

    private LocalDateTime reservationTime;

    private LocalDateTime reservationEndTime;

}
