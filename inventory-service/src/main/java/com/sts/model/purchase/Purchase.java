package com.sts.model.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sts.model.vendor.Vendor;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sts.domain.Audit;
import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase")
public class Purchase extends Audit {

    // bill info
    @Column(name = "invoice_number", nullable = false, updatable = false)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_type", nullable = false)
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "money_transaction", nullable = false)
    private MoneyTransaction moneyTransaction;

    @Column(name = "discount_amount")
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "sub_total", nullable = false)
    @Builder.Default
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Column(name = "vat_amount")
    @Builder.Default
    private BigDecimal vatAmount = BigDecimal.ZERO;

    @Column(name = "gross_total")
    @Builder.Default
    private BigDecimal grossTotal = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    // items
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    public void addItem(PurchaseItem item) {
        if (item != null) {
            purchaseItems.add(item);
            item.setPurchase(this);
        }
    }

    public void calculateTotal() {
        BigDecimal subTotalCalc = BigDecimal.ZERO;

        for (PurchaseItem item : purchaseItems) {
            item.calculateNetTotal();
            subTotalCalc = subTotalCalc.add(item.getNetTotal() != null ? item.getNetTotal() : BigDecimal.ZERO);
        }

        this.subTotal = subTotalCalc;
        BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;

        BigDecimal taxableAmount = subTotalCalc.subtract(discount).max(BigDecimal.ZERO);

        if (billingType != null && billingType.equals(BillingType.VAT)) {
            this.vatAmount = calculateVat(taxableAmount);
        } else {
            this.vatAmount = BigDecimal.ZERO;
        }

        this.grossTotal = taxableAmount.add(vatAmount != null ? vatAmount : BigDecimal.ZERO);
    }

    // VAT at 13%, rounded to 2 decimal places
    public BigDecimal calculateVat(BigDecimal taxableAmount) {
        if (taxableAmount == null || taxableAmount.compareTo(BigDecimal.ZERO) <= 0)
            return BigDecimal.ZERO;
        return taxableAmount.multiply(BigDecimal.valueOf(13))
                .divide(BigDecimal.valueOf(100))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
