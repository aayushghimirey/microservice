package com.sts.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.sts.enums.AggregateType;
import com.sts.event.factory.InvoiceEventFactory;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.helper.outbox.OutboxPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.InvoiceResponse;
import com.sts.entity.OutboxEventType;
import com.sts.event.InvoiceEvent;
import com.sts.mapper.InvoiceMapper;
import com.sts.model.Invoice;
import com.sts.repository.InvoiceRepository;
import com.sts.service.InvoiceService;
import com.sts.topics.KafkaProperties;
import com.sts.utils.constant.AppConstants;
import com.sts.utils.enums.InvoiceStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final ReferenceResolver referenceResolver;

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    private final OutboxPublisher outboxPublisher;
    private final InvoiceEventFactory invoiceEventFactory;
    private final KafkaProperties kafkaProperties;


    @Override
    @Transactional
    public InvoiceResponse proceedInvoice(UUID invoiceId) {

        Invoice invoice = referenceResolver.getOrThrow(invoiceId);

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setReservationEndTime(LocalDateTime.now());

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
