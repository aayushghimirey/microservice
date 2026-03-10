package com.sts.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.dto.response.PurchaseResponse;
import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.event.PurchaseEventFactory;
import com.sts.event.StockUpdateEvent;
import com.sts.event.StockUpdateEventFactory;
import com.sts.exception.DuplicateResourceException;
import com.sts.mapper.PurchaseMapper;
import com.sts.model.purchase.Purchase;
import com.sts.model.vendor.Vendor;
import com.sts.repository.PurchaseRepository;
import com.sts.service.PurchaseService;
import com.sts.support.OutboxPublisher;
import com.sts.support.ReferenceResolver;
import com.sts.support.VariantUnitResolver;
import com.sts.support.event.DomainEventPublisher;
import com.sts.topics.KafkaProperties;
import com.sts.utils.constant.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final PurchaseMapper purchaseMapper;

    private final VariantUnitResolver variantUnitResolver;
    private final ReferenceResolver referenceResolver;

    private final DomainEventPublisher domainEventPublisher;
    private final OutboxPublisher outboxPublisher;

    private final PurchaseEventFactory purchaseEventFactory;
    private final StockUpdateEventFactory stockUpdateEventFactory;
    private final KafkaProperties kafkaProperties;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(CreatePurchaseCommand command) {

        validateInvoiceNumber(command.invoiceNumber());

        Vendor vendor = referenceResolver.getVendorOrThrow(command.vendorId());

        command.items().forEach(item -> variantUnitResolver.getVariantUnitOrThrow(item.variantId(), item.unitId()));

        log.info("Creating purchase for vendor {}", vendor.getId()); // Added logging

        Purchase purchase = purchaseRepository.save(
                purchaseMapper.buildPurchase(command, vendor));

        publishStockUpdate(purchase);

        publishOutboxEvent(purchase);

        return purchaseMapper.toResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponse> getAllPurchases(Pageable pageable) {
        return purchaseRepository
                .findAll(pageable)
                .map(purchaseMapper::toResponse);
    }

    // ---------- PRIVATE HELPERS ----------

    private void validateInvoiceNumber(String invoiceNumber) {
        if (purchaseRepository.existsByInvoiceNumber(invoiceNumber)) {
            throw new DuplicateResourceException(
                    String.format(AppConstants.ERROR_MESSAGES.INVOICE_NUMBER_EXISTS, invoiceNumber));
        }
    }

    private void publishStockUpdate(Purchase purchase) {
        // publish stock update application event
        StockUpdateEvent stockUpdateEvent = stockUpdateEventFactory.buildFromPurchase(purchase);
        domainEventPublisher.publish(stockUpdateEvent);
    }

    private void publishOutboxEvent(Purchase purchase) {
        // serialize to outbox
        PurchaseCreatedEvent purchaseCreatedEvent = purchaseEventFactory.buildPurchaseCreatedEvent(purchase);

        outboxPublisher.publish(
                AggregateType.PURCHASE,
                purchase.getId(),
                OutboxEventType.CREATED,
                purchaseCreatedEvent,
                kafkaProperties.getTopic("purchase-event"));
    }
}