package com.sts.event;

import com.sts.dto.response.ReservationResponse;
import com.sts.mapper.ReservationMapper;
import com.sts.model.Reservation;
import com.sts.repository.ReservationRepository;
import com.sts.service.ReservationSseService;
import com.sts.topics.KafkaProperties;
import com.sts.utils.PushPendingReservations;
import com.sts.utils.enums.ReservationStatus;
import com.sts.utils.enums.TableStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class InvoiceListener {

    private static final Logger log = LoggerFactory.getLogger(InvoiceListener.class);
    private final KafkaProperties kafkaProperties;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final PushPendingReservations pushPendingReservations;

    private final ReservationSseService reservationSseService;


    @KafkaListener(
            topics = "#{@kafkaProperties.getTopic('invoice-event')}",
            containerFactory = "invoiceKafkaListenerContainerFactory"
    )
    @Transactional
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {
        log.info("Invoice event received with id {}", event.getInvoiceId());

        Reservation reservation = reservationRepository.findBySessionId(event.getSessionId());

        reservation.setStatus(ReservationStatus.COMPLETED);

        reservation.getTable().setStatus(TableStatus.OPEN);

        pushPendingReservations.pushPendingReservations();

        acknowledgment.acknowledge();

    }

}
