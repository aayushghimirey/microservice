package com.sts.event;

import com.sts.mapper.InvoiceRecordMapper;
import com.sts.topics.KafkaProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.sts.model.InvoiceRecord;
import com.sts.repository.InvoiceRecordRepository;
import com.sts.utils.constant.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceEventListener {


    private final KafkaProperties kafkaProperties;

    private final InvoiceRecordRepository invoiceRecordRepository;

    private final InvoiceRecordMapper invoiceRecordMapper;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('invoice-event')}",
            containerFactory = "invoiceKafkaListenerContainerFactory")
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {

        log.info("Invoice event received - invoiceId: {}", event.getInvoiceId());

        try {
            InvoiceRecord invoiceRecord = invoiceRecordMapper.buildInvoiceRecord(event);

            invoiceRecordRepository.save(invoiceRecord);

            acknowledgment.acknowledge();

            log.info("Invoice record saved - invoiceId: {}", event.getInvoiceId());

        } catch (Exception e) {
            log.error("Invoice event processing failed - invoiceId: {}", event.getInvoiceId(), e);
        }
    }


}
