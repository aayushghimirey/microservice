package com.sts.service.impl;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.StockUpdateFactoryRegistry;
import com.sts.exception.DuplicateResourceException;
import com.sts.filter.TenantHolder;
import com.sts.helper.event.DomainEventPublisher;
import com.sts.mapper.PurchaseMapper;
import com.sts.model.purchase.Purchase;
import com.sts.model.vendor.Vendor;
import com.sts.repository.PurchaseRepository;
import com.sts.service.PurchaseService;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.service.resolver.VariantUnitResolver;
import com.sts.shared.PurchaseOutboxPublisher;
import com.sts.utils.constant.AppConstants;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final VariantUnitResolver variantUnitResolver;
    private final ReferenceResolver referenceResolver;

    private final PurchaseMapper purchaseMapper;
    private final StockUpdateFactoryRegistry stockFactoryRegistry;

    private final DomainEventPublisher domainEventPublisher;
    private final PurchaseOutboxPublisher purchaseOutboxPublisher;

    private final RlsContext rlsContext;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(CreatePurchaseCommand command) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        checkInvoiceNumberUniqueness(command.invoiceNumber());

        Vendor vendor = referenceResolver.getVendorOrThrow(command.vendorId());

        command.items().forEach(item ->
                variantUnitResolver.getVariantUnitOrThrow(item.variantId(), item.unitId()));

        Purchase purchase = purchaseRepository.save(
                purchaseMapper.buildPurchase(command, vendor));

        /**
         publish domain event for stock update to update stock level,
         this is listen by @StockUpdateListener and processed my StockService.processStockUpdates(event)
         */
        publishStockUpdate(purchase);

        purchaseOutboxPublisher.publish(purchase);

        return purchaseMapper.toResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponse> getAllPurchases(Pageable pageable) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return purchaseRepository
                .findAll(pageable)
                .map(purchaseMapper::toResponse);
    }

    // -------------------- Helpers ----------------------

    private void checkInvoiceNumberUniqueness(String invoiceNumber) {
        if (purchaseRepository.existsByInvoiceNumber(invoiceNumber)) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ErrorMessages.INVOICE_NUMBER_EXISTS, invoiceNumber));
        }
    }

    // application event
    private void publishStockUpdate(Purchase purchase) {
        StockUpdateEvent stockUpdateEvent = stockFactoryRegistry.forPurchase(purchase);
        domainEventPublisher.publish(stockUpdateEvent);
    }


}