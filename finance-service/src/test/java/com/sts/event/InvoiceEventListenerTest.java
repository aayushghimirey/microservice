package com.sts.event;

import com.sts.event.strategy.InvoiceEventProcessingStrategy;
import com.sts.filter.TenantHolder;
import com.sts.topics.KafkaProperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceEventListenerTest {

    @Mock
    KafkaProperties kafkaProperties;
    @Mock
    InvoiceEventProcessingStrategy invoiceEventProcessingStrategy;
    @Mock
    Acknowledgment acknowledgment;

    @InjectMocks
    InvoiceEventListener invoiceEventListener;

    InvoiceEvent invoiceEvent;

    @BeforeEach
    void setUp() {
        invoiceEvent = new InvoiceEvent();
        invoiceEvent.setInvoiceId(UUID.randomUUID());
        invoiceEvent.setSessionId(UUID.randomUUID());
    }

    @Test
    void should_process_and_acknowledge() {
        TenantHolder.setTenantId(UUID.randomUUID());
        invoiceEventListener.handleInvoiceEvent(invoiceEvent, acknowledgment);

        verify(invoiceEventProcessingStrategy).process(invoiceEvent);
        verify(acknowledgment).acknowledge();
    }


}