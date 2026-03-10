package com.sts.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.InvoiceResponse;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.InvoiceEvent;
import com.sts.exception.ResourceNotFoundException;
import com.sts.mapper.InvoiceMapper;
import com.sts.mapper.OutboxMapper;
import com.sts.model.Invoice;
import com.sts.repository.InvoiceRepository;
import com.sts.repository.OutboxEventRepository;
import com.sts.service.InvoiceService;
import com.sts.topics.KafkaProperties;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.InvoiceStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper invoiceMapper;
    private final InvoiceRepository invoiceRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OutboxMapper outboxMapper;
    private final KafkaProperties kafkaProperties;
    private final OutboxEventRepository outboxEventRepository;

    @Override
    @Transactional
    public InvoiceResponse proceedInvoice(UUID invoiceId) {
        log.info(AppConstants.LOG_MESSAGES.PROCESSING_INVOICE, invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format(AppConstants.ERROR_MESSAGES.INVOICE_NOT_FOUND, invoiceId)));

        invoice.setStatus(InvoiceStatus.PAID);

        createInvoiceOutboxEvent(invoice);

        log.info(AppConstants.LOG_MESSAGES.INVOICE_PAID, invoiceId);

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllPendingInvoices() {
        return invoiceRepository.findByStatus(InvoiceStatus.PENDING).stream().map(
                invoiceMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable).map(
                invoiceMapper::toResponse);
    }

    // -- private helper
    private void createInvoiceOutboxEvent(Invoice invoice) {

        try {
            InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                    .invoiceId(invoice.getId())
                    .sessionId(invoice.getSessionId())
                    .grossTotal(invoice.getGrossTotal())
                    .reservationTime(invoice.getReservationTime())
                    .reservationEndTime(invoice.getReservationEndTime()).build();

            invoice.getItems().forEach(
                    item -> {
                        InvoiceEvent.InvoiceMenuItem menuItem = new InvoiceEvent.InvoiceMenuItem();
                        menuItem.setMenuId(item.getMenuItemId());
                        menuItem.setQuantity(item.getQuantity());

                        invoiceEvent.addItem(menuItem);
                    });

            String payload = objectMapper.writeValueAsString(invoiceEvent);

            OutboxEvent outboxEvent = outboxMapper.map(
                    "INVOICE",
                    invoice.getId().toString(),
                    OutboxEventType.CREATED,
                    payload,
                    kafkaProperties.getTopic("invoice-event"));

            outboxEventRepository.save(outboxEvent);
        } catch (Exception e) {
            log.error("Failed to create outbox event for invoice id: {}", invoice.getId(), e);
            throw new RuntimeException("Error publishing invoice event", e);
        }
    }

}
