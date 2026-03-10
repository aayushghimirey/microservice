package com.sts.event.listener;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sts.event.StockUpdateEvent;
import com.sts.model.stock.StockTransaction;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockTransactionRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.support.ReferenceResolver;
import com.sts.support.VariantUnitResolver;
import com.sts.utils.enums.StockUpdateSource;
import com.sts.utils.enums.TransactionReference;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdateProcessor {

    private final ReferenceResolver referenceResolver;
    private final VariantUnitResolver variantUnitResolver;
    private final StockVariantRepository stockVariantRepository;
    private final StockTransactionRepository stockTransactionRepository;

    public void process(StockUpdateEvent event) {
        for (var item : event.items()) {
            apply(item, event);
        }
    }

    private void apply(StockUpdateEvent.Item item, StockUpdateEvent event) {
        StockVariant variant = referenceResolver.getVariantOrThrow(item.variantId());
        VariantUnit unit = variantUnitResolver.getVariantUnitOrThrow(item.variantId(), item.unitId());

        BigDecimal delta = item.quantity().multiply(unit.getConversionRate());

        boolean isAddition = item.source() == StockUpdateSource.PURCHASE
                || item.source() == StockUpdateSource.RETURN;

        BigDecimal stockDelta = isAddition ? delta : delta.negate();

        variant.setCurrentStock(variant.getCurrentStock().add(stockDelta));
        stockVariantRepository.save(variant);

        log.info("Stock updated variantId={} delta={} balanceAfter={}",
                item.variantId(), stockDelta, variant.getCurrentStock());

        UUID referenceId = event.purchaseId() != null ? event.purchaseId() : event.invoiceId();

        StockTransaction transaction = StockTransaction.builder()
                .variantId(item.variantId())
                .unitId(item.unitId())
                .quantityChange(stockDelta)
                .balanceAfter(variant.getCurrentStock())
                .referenceId(referenceId)
                .referenceType(mapReferenceType(item.source()))
                .remark(item.source().name())
                .build();

        stockTransactionRepository.save(transaction);
    }

    private TransactionReference mapReferenceType(StockUpdateSource source) {
        return switch (source) {
            case PURCHASE -> TransactionReference.PURCHASE;
            case SALE -> TransactionReference.SALES;
            case ADJUSTMENT, RETURN -> TransactionReference.ADJUSTMENT;
        };
    }
}