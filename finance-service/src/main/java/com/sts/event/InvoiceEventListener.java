package com.sts.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceEventListener {

    @KafkaListener(
            topics = "#{@kafkaProperties.getTopic('purchase-event')}",
            containerFactory = "invoiceKafkaListenerContainerFactory"
    )
    public void listenInvoice(InvoiceEvent event, Acknowledgment acknowledgment) {
    }
}
