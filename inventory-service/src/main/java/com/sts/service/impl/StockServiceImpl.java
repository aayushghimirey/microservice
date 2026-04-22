package com.sts.service.impl;


import com.sts.dto.request.CreateStockCommand;
import com.sts.dto.request.GetStockQueryRequest;
import com.sts.dto.request.StockAdjustmentCommand;
import com.sts.dto.request.UpdateStockCommand;
import com.sts.dto.response.StockResponse;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateFactoryRegistry;
import com.sts.exception.DuplicateResourceException;
import com.sts.filter.TenantHolder;
import com.sts.helper.event.DomainEventPublisher;
import com.sts.mapper.StockMapper;
import com.sts.mapper.StockUpdateMapper;
import com.sts.model.stock.Stock;
import com.sts.model.stock.StockVariant;
import com.sts.repository.StockRepository;
import com.sts.repository.StockVariantRepository;
import com.sts.service.StockService;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.service.resolver.VariantUnitResolver;
import com.sts.service.specification.StockSpecification;
import com.sts.shared.StockOutboxPublisher;
import com.sts.utils.constant.AppConstants;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockVariantRepository stockVariantRepository;

    private final StockMapper stockMapper;
    private final StockUpdateMapper stockUpdateMapper;

    private final StockUpdateFactoryRegistry stockFactoryRegistry;
    private final StockSpecification specification;

    private final VariantUnitResolver variantUnitResolver;
    private final ReferenceResolver referenceResolver;

    private final DomainEventPublisher domainEventPublisher;

    private final StockOutboxPublisher stockOutboxPublisher;

    private final RlsContext rlsContext;


    @Override
    @Transactional
    public StockResponse createStock(CreateStockCommand command) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        assertStockNameUnique(command.name(), null);

        Stock stock = stockRepository.save(stockMapper.buildStock(command));

        // snapshot of the created stock for outbox event to menu service
        stockOutboxPublisher.publish(stock);

        return stockMapper.toResponse(stock);
    }

    @Override
    @Transactional
    public StockResponse updateStock(UUID stockId, UpdateStockCommand command) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        assertStockNameUnique(command.name(), stockId);

        Stock stock = referenceResolver.getStockOrThrow(stockId);

        stockUpdateMapper.updateStock(stock, command);

        Stock updated = stockRepository.save(stock);

        return stockMapper.toResponse(updated);
    }


    @Override
    @Transactional
    public void adjustStock(StockAdjustmentCommand command) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        StockVariant variant = referenceResolver.getVariantOrThrow(command.variantId());

        variantUnitResolver.getVariantUnitOrThrow(variant.getId(), command.unitId());

        StockUpdateEvent event = stockFactoryRegistry
                .forAdjustment(variant.getId(), command.unitId(), command.quantity(), command.isAddition(), variant.getTenantId());

        /*
         * Domain event handle by StockUpdateProcessor
         * */
        domainEventPublisher.publish(event);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse> getAllQueryStock(GetStockQueryRequest queryRequest, Pageable pageable) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Specification<Stock> spec = specification.buildSpecification(queryRequest);

        return stockRepository.findAll(spec, pageable)
                .map(stockMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockResponse.VariantResponse> getAllVariantByStockId(UUID stockId, Pageable pageable) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return stockVariantRepository.findAllByStockId(stockId, pageable)
                .map(stockMapper::toVariantResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByVariantIdAndUnitId(UUID variantId, UUID unitId) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        log.info(AppConstants.Logs.VALIDATING_VARIANT_WITH_UNIT, variantId, unitId);

        return stockVariantRepository.existsByIdAndUnits_Id(variantId, unitId);
    }

    // -------------------- Helpers ----------------------

    private void assertStockNameUnique(String name, UUID excludeId) {
        if (name == null || name.isBlank()) return;

        boolean exists = (excludeId == null)
                ? stockRepository.existsByName(name)
                : stockRepository.existsByNameAndIdNot(name, excludeId);

        if (exists) throw new DuplicateResourceException(
                String.format(AppConstants.ErrorMessages.STOCK_ALREADY_EXISTS, name));
    }


}