package com.sts.service.impl;

import com.sts.entity.OutboxEventType;
import com.sts.event.StockUpdateEvent;
import com.sts.exception.ResourceNotFoundException;
import com.sts.filter.TenantHolder;
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
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
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
    private final RlsContext rlsContext;


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void process(StockUpdateEvent event) {
        rlsContext.with("app.tenant_id", event.tenantId()).apply();
        TenantHolder.setTenantId(event.tenantId());


        log.info("Stock UPDATE event from {}", event.transactionReference());

        UUID referenceId = resolveReferenceId(event);

        Map<UUID, StockVariant> variantMap = batchFetchVariants(event.items());
        Map<UUID, VariantUnit> unitMap = batchFetchUnits(event.items());

        List<StockVariant> variantsToUpdate = new ArrayList<>();
        List<StockTransaction> transactionsToSave = new ArrayList<>();

        for (var item : event.items()) {
            processItem(item, referenceId, variantMap, unitMap,
                    variantsToUpdate, transactionsToSave, event.transactionReference());
        }

        stockVariantRepository.saveAll(variantsToUpdate);
        stockTransactionRepository.saveAll(transactionsToSave);


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
            List<StockTransaction> transactionsToSave, TransactionReference transactionReference) {

        StockVariant variant = variantMap.get(item.variantId());
        if (variant == null)
            throw new ResourceNotFoundException(
                    String.format("Variant not found: %s", item.variantId()));

        VariantUnit unit = unitMap.get(item.unitId());
        if (unit == null)
            throw new ResourceNotFoundException(
                    String.format("Unit not found: %s", item.unitId()));

        BigDecimal delta = calculateDelta(item.quantity(), unit.getConversionRate());
        BigDecimal newBalance = switch (transactionReference) {
            case PURCHASE, ADJUSTMENT -> variant.getCurrentStock().add(delta);
            case SALES -> variant.getCurrentStock().subtract(delta);
            default -> throw new IllegalArgumentException(
                    "Unsupported transaction reference: " + transactionReference);
        };

        log.debug(AppConstants.Logs.CALCULATE_STOCK_DELTA, item.variantId(), delta);


        variant.setCurrentStock(newBalance);
        variantsToUpdate.add(variant);

        transactionsToSave.add(toTransaction(item, referenceId, delta, newBalance));
        log.debug(AppConstants.Logs.CREATING_STOCK_TRANSACTION, item.variantId(), newBalance);
    }

    // ── Calculation ───────────────────────────────────────────────────────────

    private BigDecimal calculateDelta(BigDecimal quantity, BigDecimal conversionRate) {
        return quantity.multiply(conversionRate);
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
                .remark("Stock update ")
                .build();
    }


    // ── Reference resolution ──────────────────────────────────────────────────

    private UUID resolveReferenceId(StockUpdateEvent event) {
        if (event.purchaseId() != null)
            return event.purchaseId();
        if (event.invoiceId() != null)
            return event.invoiceId();

        return null;
    }

}