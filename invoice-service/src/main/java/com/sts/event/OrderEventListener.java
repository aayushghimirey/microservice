package com.sts.event;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
    private final InvoiceRepository invoiceRepository;

    @Transactional
    @KafkaListener(topics = "#{@kafkaProperties.getTopic('order-event')}", groupId = "#{@kafkaProperties.getGroup('invoice-group')}")
    public void listen(OrderCreatedEvent event, Acknowledgment acknowledgment) {

        try {
            log.info("Order event received for session: {}", event.getSessionId());

            Invoice invoice = invoiceRepository.findBySessionId(event.getSessionId());

            if (invoice != null) {
                updateInvoice(invoice, event);
            } else {
                invoice = createInvoice(event);
            }

            invoiceRepository.save(invoice);

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

        invoice.calculateGrossTotal();
    }

    private Invoice createInvoice(OrderCreatedEvent event) {

        Invoice invoice = Invoice.builder()
                .billNumber(generateBillNumber())
                .status(InvoiceStatus.PENDING)
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
        });

        invoice.calculateGrossTotal();

        return invoice;
    }

    private String generateBillNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}