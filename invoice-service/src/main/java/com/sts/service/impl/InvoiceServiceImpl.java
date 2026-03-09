package com.sts.service.impl;

import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.InvoiceEvent;
import com.sts.mapper.OutboxMapper;
import com.sts.model.Invoice;
import com.sts.repository.OutboxEventRepository;
import com.sts.topics.KafkaProperties;
import com.sts.utils.enums.InvoiceStatus;
import com.sts.dto.InvoiceResponse;
import com.sts.mapper.InvoiceMapper;
import com.sts.repository.InvoiceRepository;
import com.sts.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

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
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid invoice id, no")
        );

        invoice.setStatus(InvoiceStatus.PAID);

        createInvoiceOutboxEvent(invoice);

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllPendingInvoices() {
        return invoiceRepository.findByStatus(InvoiceStatus.PENDING).stream().map(
                invoiceMapper::toResponse
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable).map(
                invoiceMapper::toResponse
        );
    }

    // -- private helper
    public void createInvoiceOutboxEvent(Invoice invoice) {

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
                }
        );

        String payload = objectMapper.writeValueAsString(invoiceEvent);

        OutboxEvent outboxEvent = outboxMapper.map(
                "INVOICE",
                invoice.getId().toString(),
                OutboxEventType.CREATED,
                payload,
                kafkaProperties.getTopic("invoice-event")
        );

        outboxEventRepository.save(outboxEvent);
    }

}
