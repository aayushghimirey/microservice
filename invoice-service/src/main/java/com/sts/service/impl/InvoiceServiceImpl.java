package com.sts.service.impl;

import java.util.List;
import java.util.UUID;

import com.sts.enums.AggregateType;
import com.sts.event.factory.InvoiceEventFactory;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.shared.outbox.OutboxPublisher;
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

    private final ReferenceResolver referenceResolver;

    private final OutboxPublisher outboxPublisher;

    private final InvoiceEventFactory invoiceEventFactory;

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

        Invoice invoice = referenceResolver.getOrThrow(invoiceId);

        invoice.setStatus(InvoiceStatus.PAID);

        publishOutboxEvent(invoice);

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
    private void publishOutboxEvent(Invoice invoice) {

        InvoiceEvent event = invoiceEventFactory.buildInvoiceEvent(invoice);

        outboxPublisher.publish(
                AggregateType.INVOICE,
                invoice.getId(),
                OutboxEventType.CREATED,
                event,
                kafkaProperties.getTopic(AppConstants.KAFKA_TOPIC_INVOICE_EVENT)
        );

    }

}
