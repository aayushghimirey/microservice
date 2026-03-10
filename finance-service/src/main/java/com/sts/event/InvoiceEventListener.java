package com.sts.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.sts.model.InvoiceRecord;
import com.sts.repository.InvoiceRecordRepository;
import com.sts.utils.constant.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceEventListener {

    private final InvoiceRecordRepository invoiceRecordRepository;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('invoice-event')}",
            containerFactory = "invoiceKafkaListenerContainerFactory")
    public void listenInvoice(InvoiceEvent event, Acknowledgment acknowledgment) {

        log.info(AppConstants.LOG_MESSAGES.INVOICE_EVENT_RECEIVED, event.getInvoiceId());

        try {
            InvoiceRecord invoiceRecord = buildInvoiceRecord(event);

            invoiceRecordRepository.save(invoiceRecord);

            acknowledgment.acknowledge();

            log.info(AppConstants.LOG_MESSAGES.INVOICE_RECORD_SAVED, event.getInvoiceId());

        } catch (Exception e) {
            log.error(AppConstants.LOG_MESSAGES.INVOICE_EVENT_FAILED, event.getInvoiceId(), e);
            throw e;
        }
    }

    // -- private helper
    public InvoiceRecord buildInvoiceRecord(InvoiceEvent event) {
        return InvoiceRecord.builder()
                .invoiceId(event.getInvoiceId())
                .reservationTime(event.getReservationTime())
                .reservationEndTime(event.getReservationEndTime())
                .grossTotal(event.getGrossTotal()).build();
    }

}
