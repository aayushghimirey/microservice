package com.sts.event;


import com.sts.constant.enums.InvoiceStatus;
import com.sts.model.Invoice;
import com.sts.repository.InvoiceRepository;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final KafkaProperties kafkaProperties;
    private final InvoiceRepository invoiceRepository;

    @KafkaListener(
            topics = "#{@kafkaProperties.getTopic('order-event')}",
            groupId = "#{@kafkaProperties.getGroup('invoice-group')}"
    )
    public void listen(ConsumerRecord<String, OrderCreatedEvent> record, Acknowledgment acknowledgment) {

        Invoice bySessionId = invoiceRepository.findBySessionId(record.value().getSessionId());
        if (bySessionId != null) {
            bySessionId.setBillNumber("Bill-" + record.offset());
            bySessionId.setStatus(InvoiceStatus.PENDING);
            bySessionId.setSubTotal(record.value().getBillAmount());
            bySessionId.setSessionId(record.value().getSessionId());
            bySessionId.setTableId(record.value().getTableId());
            bySessionId.setReservationTime(record.value().getReservationTime());
            bySessionId.calculateGrossTotal();

            invoiceRepository.save(bySessionId);
            acknowledgment.acknowledge();

            log.info("Invoice updated success");
        } else {

            Invoice invoice = new Invoice();
            invoice.setBillNumber("Bill-" + record.offset());
            invoice.setStatus(InvoiceStatus.PENDING);
            invoice.setSubTotal(record.value().getBillAmount());
            invoice.setSessionId(record.value().getSessionId());
            invoice.setTableId(record.value().getTableId());
            invoice.setReservationTime(record.value().getReservationTime());
            invoice.calculateGrossTotal();


            invoiceRepository.save(invoice);
            acknowledgment.acknowledge();

            log.info("Invoice save success");
        }


    }


}
