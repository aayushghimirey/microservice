package com.sts.stock.application.event;

import com.sts.stock.domain.enums.TransactionReference;
import com.sts.stock.domain.model.StockTransaction;
import com.sts.stock.domain.model.StockVariant;
import com.sts.stock.domain.model.VariantUnit;
import com.sts.stock.domain.repository.StockTransactionRepository;
import com.sts.stock.domain.repository.StockVariantRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.UUID;

/*
 *
 * This listener handles stock updates triggered by both purchase events and manual adjustments.
 *  It listens for StockUpdateEvent, which contains details about the stock changes.
 *
 * */
@Component
@AllArgsConstructor
public class StockUpdateListener {

    private static final Logger log = LoggerFactory.getLogger(StockUpdateListener.class);
    private final StockVariantRepository stockVariantRepository;
    private final StockTransactionRepository stockTransactionRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(StockUpdateEvent stockUpdateEvent) {
        if (stockUpdateEvent.purchaseId() != null) {
            processAddition(stockUpdateEvent);
        } else {
            processDeduction(stockUpdateEvent);
        }
    }

    private void processAddition(StockUpdateEvent stockUpdateEvent) {
        log.info("Processing stock addition for purchaseId {}", stockUpdateEvent.purchaseId());
        for (var item : stockUpdateEvent.event()) {
            StockVariant variant = getVariantAndUnit(item);

            BigDecimal delta = item.quantity().multiply(getConversionRate(variant, item));
            variant.setCurrentStock(variant.getCurrentStock().add(delta));
            stockVariantRepository.saveAndFlush(variant);

            saveTransaction(item, variant, delta, stockUpdateEvent.purchaseId(), TransactionReference.PURCHASE);
        }
    }

    private void processDeduction(StockUpdateEvent stockUpdateEvent) {
        log.info("Processing stock deduction for adjustment event");
        for (var item : stockUpdateEvent.event()) {
            StockVariant variant = getVariantAndUnit(item);

            BigDecimal delta = item.quantity().multiply(getConversionRate(variant, item));
            variant.setCurrentStock(variant.getCurrentStock().subtract(delta));
            stockVariantRepository.saveAndFlush(variant);

            saveTransaction(item, variant, delta.negate(), null, TransactionReference.ADJUSTMENT);
        }
    }

    private StockVariant getVariantAndUnit(StockUpdateEvent.Info item) {
        StockVariant variant = stockVariantRepository.findById(item.variantId())
                .orElseThrow(() -> new IllegalArgumentException("Variant not found: " + item.variantId()));
        variant.getUnits().stream()
                .filter(u -> u.getId().equals(item.unitId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unit not found: " + item.unitId()));
        return variant;
    }

    private BigDecimal getConversionRate(StockVariant variant, StockUpdateEvent.Info item) {
        return variant.getUnits().stream()
                .filter(u -> u.getId().equals(item.unitId()))
                .findFirst()
                .get()
                .getConversionRate();
    }


    private void saveTransaction(StockUpdateEvent.Info item, StockVariant variant, BigDecimal delta,
                                 UUID referenceId, TransactionReference type) {
        StockTransaction transaction = StockTransaction.builder()
                .variantId(item.variantId())
                .unitId(item.unitId())
                .quantityChange(delta)
                .balanceAfter(variant.getCurrentStock())
                .referenceId(referenceId)
                .referenceType(type)
                .remark(item.reason())
                .build();
        stockTransactionRepository.save(transaction);

        log.info("Transaction saved: variant {} delta {}", item.variantId(), delta);
    }
}