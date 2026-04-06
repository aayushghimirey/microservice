package com.sts.event.strategy;

import com.sts.event.InvoiceEvent;
import com.sts.filter.TenantHolder;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.model.InvoiceRecord;
import com.sts.repository.InvoiceRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceEventProcessingStrategyTest {

    @Mock
    InvoiceRecordRepository invoiceRecordRepository;
    @Mock
    InvoiceRecordMapper invoiceRecordMapper;

    @InjectMocks
    InvoiceEventProcessingStrategy strategy;


    InvoiceEvent invoiceEvent;
    InvoiceEvent invoiceResponse;

    @BeforeEach
    void setUp() {
        invoiceEvent = new InvoiceEvent();
        invoiceResponse = new InvoiceEvent();
        invoiceEvent.setInvoiceId(UUID.randomUUID());
        invoiceEvent.setSessionId(UUID.randomUUID());
    }

    @Test
    void process_valid_event() {
        TenantHolder.setTenantId(UUID.randomUUID());

        InvoiceEvent event = invoiceEvent;
        InvoiceRecord expected = mock(InvoiceRecord.class);

        when(invoiceRecordRepository.findByInvoiceId(event.getInvoiceId()))
                .thenReturn(Optional.empty());
        when(invoiceRecordMapper.buildEntity(event)).thenReturn(expected);
        when(invoiceRecordRepository.save(expected)).thenReturn(expected);


        strategy.process(event);

        verify(invoiceRecordMapper).buildEntity(event);
        verify(invoiceRecordRepository).save(expected);

    }

}