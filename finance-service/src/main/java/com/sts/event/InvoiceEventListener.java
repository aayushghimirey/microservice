package com.sts.event;


import com.sts.model.InvoiceRecord;
import com.sts.repository.InvoiceRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceEventListener {

    private final InvoiceRecordRepository invoiceRecordRepository;

    @KafkaListener(
            topics = "#{@kafkaProperties.getTopic('invoice-event')}",
            containerFactory = "invoiceKafkaListenerContainerFactory"
    )
    public void listenInvoice(InvoiceEvent event, Acknowledgment acknowledgment) {

        log.info("Invoice event received with id {}", event.getInvoiceId());


        InvoiceRecord invoiceRecord = InvoiceRecord.builder()
                .invoiceId(event.getInvoiceId())
                .reservationTime(event.getReservationTime())
                .reservationTime(event.getReservationEndTime())
                .grossTotal(event.getGrossTotal()).build();

        invoiceRecordRepository.save(invoiceRecord);

        acknowledgment.acknowledge();

    }
}
