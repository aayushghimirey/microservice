package com.sts.event.listener;

import java.math.BigDecimal;

import com.sts.event.OrderCreatedEvent;
import com.sts.filter.TenantHolder;
import com.sts.mapper.InvoiceMapper;
import com.sts.model.InvoiceItemIngredient;
import com.sts.utils.constant.AppConstants;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sts.model.Invoice;
import com.sts.model.InvoiceItem;
import com.sts.repository.InvoiceRepository;
import com.sts.topics.KafkaProperties;
import com.sts.utils.enums.InvoiceStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final KafkaProperties kafkaProperties;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final RlsContext rlsContext;

    @Transactional
    @KafkaListener(topics = "#{@kafkaProperties.getTopic('order-event')}", groupId = "#{@kafkaProperties.getGroup('invoice-group')}")
    public void listen(OrderCreatedEvent event, Acknowledgment acknowledgment) {

        TenantHolder.setTenantId(event.getTenantId());

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();


        log.info(AppConstants.LOG_MESSAGES.ORDER_EVENT_MESSAGE, event.getSessionId(), event.getTenantId());

        try {
            Invoice invoice = invoiceRepository.findBySessionId(event.getSessionId());

            if (invoice != null) {
                updateInvoice(invoice, event);
            } else {
                invoice = createInvoice(event);
            }

            invoiceRepository.save(invoice);

            simpMessagingTemplate.convertAndSendToUser(invoice.getTenantId().toString(), "/queue/invoices", invoiceMapper.toResponse(invoice));

            log.info("Invoice processed successfully for session: {}", event.getSessionId());

            acknowledgment.acknowledge();

        } catch (Exception e) {

            log.error("Error processing order event {}", event, e);
            throw e;
        }
    }

    private void updateInvoice(Invoice invoice, OrderCreatedEvent event) {

        invoice.setBillNumber(generateBillNumber());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setSubTotal(event.getBillAmount());
        invoice.setSessionId(event.getSessionId());
        invoice.setTableId(event.getTableId());
        invoice.setReservationTime(event.getReservationTime());

//        invoice
//
//        event.getItems().forEach(ing -> {
//
//        });

        invoice.calculateGrossTotal();
    }

    private Invoice createInvoice(OrderCreatedEvent event) {

        Invoice invoice = Invoice.builder()
                .billNumber(generateBillNumber())
                .status(InvoiceStatus.PENDING)
                .reservationId(event.getReservationId())
                .subTotal(event.getBillAmount())
                .discountAmount(BigDecimal.ZERO)
                .sessionId(event.getSessionId())
                .tableId(event.getTableId())
                .reservationTime(event.getReservationTime())
                .build();

        event.getItems().forEach(item -> {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setMenuItemId(item.getMenuId());
            invoiceItem.setQuantity(item.getQuantity());

            invoice.addItem(invoiceItem);

            item.getIngredient().forEach(ing -> {
                InvoiceItemIngredient ingredient = InvoiceItemIngredient.builder()
                        .variantId(ing.getVariantId())
                        .unitId(ing.getUnitId())
                        .quantity(ing.getQuantity())
                        .build();
                invoiceItem.addIngredient(ingredient);
            });

            invoice.addItem(invoiceItem);

        });

        invoice.calculateGrossTotal();

        return invoice;
    }

    private String generateBillNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}