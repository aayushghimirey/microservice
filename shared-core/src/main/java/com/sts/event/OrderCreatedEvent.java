package com.sts.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    private List<MenuItems> items;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MenuItems {
        private UUID variantId;

        private UUID unitId;

        private double quantity;
    }

}
