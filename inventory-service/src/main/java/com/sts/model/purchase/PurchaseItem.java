package com.sts.model.purchase;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sts.domain.Audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "purchase_items")
public class PurchaseItem extends Audit {

    @Column(name = "variant_id", nullable = false, updatable = false)
    private UUID variantId;

    @Column(name = "unit_id", nullable = false, updatable = false)
    private UUID unitId;

    @Column(name = "quantity", nullable = false, updatable = false)
    private BigDecimal quantity;

    @Column(name = "per_unit_price", nullable = false, updatable = false)
    private BigDecimal perUnitPrice;

    @Column(name = "discount_amount", updatable = false)
    private BigDecimal discountAmount;

    @Column(name = "sub_total", nullable = false, updatable = false)
    private BigDecimal subTotal;

    @Column(name = "net_total", nullable = false, updatable = false)
    private BigDecimal netTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    public void calculateNetTotal() {
        BigDecimal safeQuantity = quantity != null ? quantity : BigDecimal.ZERO;
        BigDecimal safePrice = perUnitPrice != null ? perUnitPrice : BigDecimal.ZERO;
        BigDecimal safeDiscount = discountAmount != null ? discountAmount : BigDecimal.ZERO;

        this.subTotal = safeQuantity.multiply(safePrice);
        this.netTotal = this.subTotal.subtract(safeDiscount.max(BigDecimal.ZERO));
    }
}
