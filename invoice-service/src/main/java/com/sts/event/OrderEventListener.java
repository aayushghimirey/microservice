package com.sts.event;


import com.sts.model.InvoiceItem;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.InvoiceStatus;
import com.sts.model.Invoice;
import com.sts.repository.InvoiceRepository;
import com.sts.topics.KafkaProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final KafkaProperties kafkaProperties;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    @KafkaListener(
            topics = "#{@kafkaProperties.getTopic('order-event')}",
            groupId = "#{@kafkaProperties.getGroup('invoice-group')}"
    )
    public void listen(OrderCreatedEvent event, Acknowledgment acknowledgment) {

        try {
            log.info(String.format(AppConstants.LOG_MESSAGES.ORDER_EVENT_MESSAGE, event.getSessionId()));

            Invoice invoice = invoiceRepository.findBySessionId(event.getSessionId());

            if (invoice != null) {
                updateInvoice(invoice, event);
            } else {
                invoice = createInvoice(event);
            }

            invoiceRepository.save(invoice);

            log.info(String.format(AppConstants.LOG_MESSAGES.INVOICE_PROCESSED_SUCCESS, event.getSessionId()));

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

        event.getItems().forEach(item ->
        {
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