package com.sts.service.impl;

import com.sts.dto.request.CreatePurchaseCommand;
import com.sts.entity.OutboxEventType;
import com.sts.enums.AggregateType;
import com.sts.enums.BillingType;
import com.sts.enums.MoneyTransaction;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.event.StockUpdateEvent;
import com.sts.event.factory.PurchaseEventFactory;
import com.sts.event.factory.StockUpdateEventFactory;
import com.sts.mapper.PurchaseMapper;
import com.sts.model.purchase.Purchase;
import com.sts.model.stock.VariantUnit;
import com.sts.model.vendor.Vendor;
import com.sts.repository.PurchaseRepository;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.service.resolver.VariantUnitResolver;
import com.sts.helper.event.DomainEventPublisher;
import com.sts.helper.outbox.OutboxPublisher;
import com.sts.topics.KafkaProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock
    PurchaseRepository purchaseRepository;

    @Mock
    PurchaseMapper purchaseMapper;

    @Mock
    StockUpdateEventFactory stockUpdateEventFactory;

    @Mock
    ReferenceResolver referenceResolver;

    @Mock
    PurchaseEventFactory purchaseEventFactory;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Mock
    OutboxPublisher outboxPublisher;

    @Mock
    VariantUnitResolver variantUnitResolver;

    @Mock
    DomainEventPublisher domainEventPublisher;

    @Mock
    KafkaProperties kafkaProperties;


    @InjectMocks
    PurchaseServiceImpl purchaseService;


    UUID variantId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID unitId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID vendorId = UUID.fromString("11111111-1111-2222-1111-111111111111");
    UUID purchaseId = UUID.fromString("11111111-1122-2222-1111-111111111111");


    CreatePurchaseCommand buildCommand() {
        CreatePurchaseCommand.PurchaseItemCommand item = CreatePurchaseCommand.PurchaseItemCommand.builder()
                .variantId(variantId)
                .unitId(unitId)
                .quantity(BigDecimal.valueOf(3))
                .perUnitPrice(BigDecimal.valueOf(200))
                .discountAmount(BigDecimal.ZERO).build();
        return CreatePurchaseCommand.builder()
                .invoiceNumber("B-01")
                .billingType(BillingType.PAN)
                .vendorId(vendorId)
                .moneyTransaction(MoneyTransaction.CASH)
                .discountAmount(BigDecimal.valueOf(0))
                .items(List.of(item)).build();
    }

    @Test
    void createPurchase() {
        CreatePurchaseCommand command = buildCommand();

        Vendor vendor = mock(Vendor.class);
        VariantUnit variantUnit = mock(VariantUnit.class);

        Purchase purchase = mock(Purchase.class);
        Purchase savedPurchase = mock(Purchase.class);

        StockUpdateEvent event = mock(StockUpdateEvent.class);
        PurchaseCreatedEvent purchaseCreatedEvent = mock(PurchaseCreatedEvent.class);

        when(purchaseRepository.existsByInvoiceNumber(command.invoiceNumber())).thenReturn(false);

        when(referenceResolver.getVendorOrThrow(command.vendorId())).thenReturn(vendor);

        when(variantUnitResolver.getVariantUnitOrThrow(variantId, unitId)).thenReturn(variantUnit);

        when(purchaseMapper.buildPurchase(command, vendor)).thenReturn(purchase);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(savedPurchase);

        when(stockUpdateEventFactory.buildFromPurchase(savedPurchase)).thenReturn(event);

        when(savedPurchase.getId()).thenReturn(purchaseId);

        when(purchaseEventFactory.buildPurchaseCreatedEvent(savedPurchase))
                .thenReturn(purchaseCreatedEvent);

        when(kafkaProperties.getTopic(any())).thenReturn("test-topic");
        // act
        purchaseService.createPurchase(command);

        // verify
        verify(domainEventPublisher).publish(event);
        verify(outboxPublisher)
                .publish(
                        eq(AggregateType.PURCHASE),
                        any(),
                        eq(OutboxEventType.CREATED),
                        eq(purchaseCreatedEvent),
                        any()
                );
    }

}
