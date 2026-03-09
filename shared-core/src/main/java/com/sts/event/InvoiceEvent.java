package com.sts.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceEvent {

    private UUID invoiceId;

    private UUID sessionId;

    @Builder.Default
    private BigDecimal grossTotal = BigDecimal.ZERO;

    private LocalDateTime reservationTime;

    private LocalDateTime reservationEndTime;

    @Builder.Default
    private List<InvoiceEvent.InvoiceMenuItem> items = new ArrayList<>();

    public void addItem(InvoiceEvent.InvoiceMenuItem item) {
        items.add(item);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InvoiceMenuItem {
        private UUID menuId;

        private double quantity;
    }

}
