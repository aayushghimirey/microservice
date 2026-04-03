package com.sts.service.impl;

import com.sts.dto.InvoiceRecordResponse;
import com.sts.mapper.InvoiceRecordMapper;
import com.sts.model.InvoiceRecord;
import com.sts.repository.InvoiceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceRecordServiceImplTest {

    @Mock
    InvoiceRecordRepository invoiceRecordRepository;
    @Mock
    InvoiceRecordMapper invoiceRecordMapper;

    @InjectMocks
    InvoiceRecordServiceImpl invoiceRecordService;


    @Test
    void should_return_normal() {

        InvoiceRecord entity = new InvoiceRecord();
        InvoiceRecordResponse response = new InvoiceRecordResponse(
                UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(100),
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        );
        Pageable pageable = PageRequest.of(0, 10);


        Page<InvoiceRecord> entityPage = new PageImpl<>(List.of(entity));

        when(invoiceRecordRepository.findAll(pageable)).thenReturn(entityPage);
        when(invoiceRecordMapper.toResponse(entity)).thenReturn(response);

        Page<InvoiceRecordResponse> result = invoiceRecordService.getAllRecords(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(invoiceRecordMapper).toResponse(entity);
    }

}