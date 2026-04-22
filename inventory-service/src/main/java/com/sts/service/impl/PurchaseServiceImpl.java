package com.sts.service.impl;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.request.GetPurchaseQueryRequest;
import com.sts.dto.response.PurchaseInfo;
import com.sts.dto.response.PurchaseResponse;
import com.sts.enums.DateSelection;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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

        // validating purchase items
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
    public Page<PurchaseResponse> getAllPurchases(GetPurchaseQueryRequest request, Pageable pageable) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Specification<Purchase> purchaseSpecification = buildSpecification(request);

        return purchaseRepository
                .findAll(purchaseSpecification, pageable)
                .map(purchaseMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PurchaseInfo getPurchaseDashboardInfo(DateSelection dateSelection) {

        ZoneId zoneId = ZoneId.of("Asia/Kathmandu");

        Instant instant = switch (dateSelection) {

            case TODAY -> LocalDate.now(zoneId)
                    .atStartOfDay(zoneId)
                    .toInstant();

            case WEEK -> Instant.now().minus(7, ChronoUnit.DAYS);

            case MONTH -> Instant.now().minus(1, ChronoUnit.MONTHS);
        };

        BigDecimal purchaseAmountSum =
                purchaseRepository.findPurchaseAmountSum(instant);

        Long countPurchase =
                purchaseRepository.countPurchase(instant);

        return new PurchaseInfo(
                countPurchase,
                purchaseAmountSum != null ? purchaseAmountSum : BigDecimal.ZERO
        );
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


    private Specification<Purchase> buildSpecification(GetPurchaseQueryRequest request) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (request.invoiceNumber() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(root.get("invoiceNumber"), "%" + request.invoiceNumber() + "%"));
            }

            if (request.vendorId() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("vendor").get("id"), request.vendorId()));
            }

            if (request.billingType() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("billingType"), request.billingType()));
            }

            if (request.moneyTransaction() != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("moneyTransaction"), request.moneyTransaction()));
            }

            return predicates;
        };
    }

}