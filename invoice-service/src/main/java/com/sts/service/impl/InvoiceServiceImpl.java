package com.sts.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sts.domain.BusinessDetailResponse;
import com.sts.dto.CreateInvoiceCommand;
import com.sts.dto.InvoiceSearchRequest;
import com.sts.enums.AggregateType;
import com.sts.event.factory.InvoiceEventFactory;
import com.sts.feign.BusinessDetailClient;
import com.sts.filter.TenantHolder;
import com.sts.service.InvoicePosPrint;
import com.sts.service.resolver.ReferenceResolver;
import com.sts.helper.outbox.OutboxPublisher;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final BusinessDetailClient businessDetailClient;

    private final OutboxPublisher outboxPublisher;
    private final InvoiceEventFactory invoiceEventFactory;
    private final KafkaProperties kafkaProperties;

    private final RlsContext rlsContext;


    @Override
    @Transactional
    public InvoiceResponse proceedInvoice(UUID invoiceId, CreateInvoiceCommand command) {

        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Invoice invoice = referenceResolver.getOrThrow(invoiceId);

        invoice.getItems().stream()
                .filter(item -> command.hiddenItemIds().contains(item.getId()))
                .forEach(item -> item.setPrintable(false));

        invoice.setInvoiceType(command.invoiceType());
        invoice.setPaymentMethod(command.moneyTransaction());
        invoice.setDiscountAmount(command.discount());

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setReservationEndTime(Instant.now());

        invoice.calculateGrossTotal();

        publishOutboxEvent(invoice);

        InvoiceResponse response = invoiceMapper.toResponse(invoice);

        simpMessagingTemplate.convertAndSendToUser(invoice.getTenantId().toString(), "/queue/invoices", response);

        return response;

    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllPendingInvoices() {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        return invoiceRepository.findByStatus(InvoiceStatus.PENDING).stream().map(invoiceMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getAllInvoices(InvoiceSearchRequest request, Pageable pageable) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Specification<Invoice> invoiceSpecification = buildSpecification(request);

        return invoiceRepository.findAll(invoiceSpecification, pageable).map(invoiceMapper::toResponse);
    }

    @Override
    @Transactional
    public String printInvoice(UUID invoiceId) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));

        log.info("Fetching business details for tenantId: {}", invoice.getTenantId());
        BusinessDetailResponse bussinessDetail = businessDetailClient.getBusinessDetail(invoice.getTenantId()).getBody().getData();
        log.info("Received business details: {}", bussinessDetail);

        return InvoicePosPrint.printInvoice(invoice, bussinessDetail);
    }

    // -- private helper
    private void publishOutboxEvent(Invoice invoice) {

        InvoiceEvent event = invoiceEventFactory.buildInvoiceEvent(invoice);

        outboxPublisher.publish(
                AggregateType.INVOICE, invoice.getId(), OutboxEventType.CREATED, event,
                kafkaProperties.getTopic(AppConstants.KAFKA_TOPIC_INVOICE_EVENT));

    }


    private Specification<Invoice> buildSpecification(InvoiceSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.billNumber() != null && !request.billNumber().isBlank()) {
                predicates.add(cb.equal(root.get("billNumber"), request.billNumber()));
            }

            if (request.sessionId() != null) {
                predicates.add(cb.equal(root.get("sessionId"), request.sessionId()));
            }

            if (request.tableId() != null) {
                predicates.add(cb.equal(root.get("tableId"), request.tableId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
