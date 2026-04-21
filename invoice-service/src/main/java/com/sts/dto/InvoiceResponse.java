package com.sts.dto;

import com.sts.utils.enums.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private UUID id;

    private String billNumber;

    private UUID tableId;

    private UUID sessionId;

    private InvoiceStatus status;

    private BigDecimal subTotal = BigDecimal.ZERO;

    private BigDecimal grossTotal = BigDecimal.ZERO;

    private Instant reservationTime;

    private Instant reservationEndTime;

    @Builder.Default
    private List<InvoiceItemResponse> items = new ArrayList<>();

    public void addItem(InvoiceItemResponse item) {
        if (item == null) return;

        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(item);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class InvoiceItemResponse {

        private UUID id;
        private String name;
        private double quantity;
        private BigDecimal price;
    }
}
