package com.sts.event;

import com.sts.event.strategy.InvoiceEventProcessingStrategy;
import com.sts.filter.TenantHolder;
import com.sts.topics.KafkaProperties;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceEventListener {


    private final KafkaProperties kafkaProperties;
    private final InvoiceEventProcessingStrategy invoiceEventProcessingStrategy;
    private final RlsContext rlsContext;

    @KafkaListener(topics = "${app.kafka.topics.invoice-event}",
            containerFactory = "invoiceKafkaListenerContainerFactory")


    public void handleInvoiceEvent(InvoiceEvent event, Acknowledgment acknowledgment) {


        log.info("Invoice event received - invoiceId: {}", event.getInvoiceId());

        invoiceEventProcessingStrategy.process(event);

        acknowledgment.acknowledge();

        log.info("Invoice record saved - invoiceId: {}", event.getInvoiceId());

        TenantHolder.clear();

    }


}
