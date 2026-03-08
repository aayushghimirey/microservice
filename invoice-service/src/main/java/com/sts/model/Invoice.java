package com.sts.model;

import com.sts.utils.enums.InvoiceStatus;
import com.sts.domain.Audit;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Invoice extends Audit {

    private String billNumber;

    private UUID tableId;

    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal subTotal = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal grossTotal = BigDecimal.ZERO;

    private LocalDateTime reservationTime;

    private LocalDateTime reservationEndTime;

    @Builder.Default
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    public void calculateGrossTotal() {
        BigDecimal sub = subTotal == null ? BigDecimal.ZERO : subTotal;
        BigDecimal discount = discountAmount == null ? BigDecimal.ZERO : discountAmount;

        this.grossTotal = sub.subtract(discount);
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
    }

}