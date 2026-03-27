package com.sts.service.impl;

import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.event.StockEvent;
import com.sts.event.StockUpdateEvent;
import com.sts.exception.ResourceNotFoundException;
import com.sts.helper.outbox.OutboxPublisher;
import com.sts.mapper.StockMapper;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockTransaction;
import com.sts.model.stock.StockVariant;
import com.sts.model.stock.VariantUnit;
import com.sts.repository.StockTransactionRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.repository.VariantUnitRepository;
import com.sts.service.StockUpdateProcessor;
import com.sts.shared.StockOutboxPublisher;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.StockUpdateSource;
import com.sts.utils.enums.TransactionReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockUpdateProcessorImpl implements StockUpdateProcessor {

    private final StockVariantRepository stockVariantRepository;
    private final VariantUnitRepository variantUnitRepository;
    private final StockTransactionRepository stockTransactionRepository;

    private final StockOutboxPublisher stockOutboxPublisher;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(StockUpdateEvent event) {
        UUID referenceId = resolveReferenceId(event);

        Map<UUID, StockVariant> variantMap = batchFetchVariants(event.items());
        Map<UUID, VariantUnit> unitMap = batchFetchUnits(event.items());

        List<StockVariant> variantsToUpdate = new ArrayList<>();
        List<StockTransaction> transactionsToSave = new ArrayList<>();
        Set<Stock> affectedStocks = new LinkedHashSet<>();

        for (var item : event.items()) {
            processItem(item, referenceId, variantMap, unitMap,
                    variantsToUpdate, transactionsToSave, affectedStocks);
        }

        stockVariantRepository.saveAll(variantsToUpdate);
        stockTransactionRepository.saveAll(transactionsToSave);

        // publish once per unique stock — consumed by menu-service to update IngredientStock snapshot
        affectedStocks.forEach(stock -> publishOutboxEvent(stock, OutboxEventType.UPDATED));
    }


    private Map<UUID, StockVariant> batchFetchVariants(List<StockUpdateEvent.StockUpdateItem> items) {
        Set<UUID> ids = items.stream()
                .map(StockUpdateEvent.StockUpdateItem::variantId)
                .collect(Collectors.toSet());

        return stockVariantRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(StockVariant::getId, Function.identity()));
    }

    private Map<UUID, VariantUnit> batchFetchUnits(List<StockUpdateEvent.StockUpdateItem> items) {
        Set<UUID> ids = items.stream()
                .map(StockUpdateEvent.StockUpdateItem::unitId)
                .collect(Collectors.toSet());

        return variantUnitRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(VariantUnit::getId, Function.identity()));
    }

    // ── Processing ────────────────────────────────────────────────────────────

    private void processItem(
            StockUpdateEvent.StockUpdateItem item,
            UUID referenceId,
            Map<UUID, StockVariant> variantMap,
            Map<UUID, VariantUnit> unitMap,
            List<StockVariant> variantsToUpdate,
            List<StockTransaction> transactionsToSave,
            Set<Stock> affectedStocks) {

        StockVariant variant = variantMap.get(item.variantId());
        if (variant == null) throw new ResourceNotFoundException(
                String.format("Variant not found: %s", item.variantId()));

        VariantUnit unit = unitMap.get(item.unitId());
        if (unit == null) throw new ResourceNotFoundException(
                String.format("Unit not found: %s", item.unitId()));

        BigDecimal delta = calculateDelta(item.quantity(), unit.getConversionRate(), item.source());
        BigDecimal newBalance = variant.getCurrentStock().add(delta);

        log.debug(AppConstants.Logs.CALCULATE_STOCK_DELTA, item.variantId(), item.source(), delta);

        variant.setCurrentStock(newBalance);
        variantsToUpdate.add(variant);
        affectedStocks.add(variant.getStock());

        transactionsToSave.add(toTransaction(item, referenceId, delta, newBalance));
        log.debug(AppConstants.Logs.CREATING_STOCK_TRANSACTION, item.variantId(), newBalance);
    }

    // ── Calculation ───────────────────────────────────────────────────────────

    private BigDecimal calculateDelta(BigDecimal quantity, BigDecimal conversionRate, StockUpdateSource source) {
        return quantity.multiply(conversionRate).multiply(source.getMultiplier());
    }

    // ── Mapping ───────────────────────────────────────────────────────────────

    private StockTransaction toTransaction(
            StockUpdateEvent.StockUpdateItem item,
            UUID referenceId,
            BigDecimal delta,
            BigDecimal balanceAfter) {
        return StockTransaction.builder()
                .variantId(item.variantId())
                .unitId(item.unitId())
                .quantityChange(delta)
                .balanceAfter(balanceAfter)
                .referenceId(referenceId)
                .referenceType(toReferenceType(item.source()))
                .remark(item.source().name())
                .build();
    }

    private TransactionReference toReferenceType(StockUpdateSource source) {
        return switch (source) {
            case PURCHASE -> TransactionReference.PURCHASE;
            case SALE -> TransactionReference.SALES;
            case ADJUSTMENT,
                 RETURN -> TransactionReference.ADJUSTMENT;
        };
    }

    // ── Reference resolution ──────────────────────────────────────────────────

    private UUID resolveReferenceId(StockUpdateEvent event) {
        if (event.purchaseId() != null) return event.purchaseId();
        if (event.invoiceId() != null) return event.invoiceId();

        return null;
    }

    // ── Outbox publishing ─────────────────────────────────────────────────────

    private void publishOutboxEvent(Stock stock, OutboxEventType eventType) {
        stockOutboxPublisher.publish(stock, eventType);

    }
}