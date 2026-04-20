package com.sts.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private UUID tenantId;

    @Builder.Default
    private BigDecimal billAmount = BigDecimal.ZERO;

    private Instant reservationTime;

    private Instant reservationEndTime;

    @Builder.Default
    private List<MenuItem> items = new ArrayList<>();

    public void addItem(MenuItem item) {
        items.add(item);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MenuItem {

        private UUID menuId;

        private double quantity;

        private List<MenuIngredientResponse> ingredient;
    }

}
