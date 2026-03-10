package com.sts.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sts.mapper.ReservationMapper;
import com.sts.model.Reservation;
import com.sts.repository.ReservationRepository;
import com.sts.service.ReservationSseService;
import com.sts.topics.KafkaProperties;
import com.sts.utils.PushPendingReservations;
import com.sts.utils.contant.AppConstants;
import com.sts.utils.enums.ReservationStatus;
import com.sts.utils.enums.TableStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class InvoiceListener {

    private final KafkaProperties kafkaProperties;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final PushPendingReservations pushPendingReservations;

    private final ReservationSseService reservationSseService;

    @KafkaListener(topics = "#{@kafkaProperties.getTopic('invoice-event')}", containerFactory = "invoiceKafkaListenerContainerFactory")
    @Transactional
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {
        log.info(AppConstants.LOG_MESSAGES.INVOICE_EVENT_RECEIVED, event.getInvoiceId());

        try {
            Reservation reservation = reservationRepository.findBySessionId(event.getSessionId());

            if (reservation == null) {
                log.warn(AppConstants.LOG_MESSAGES.SESSION_NOT_FOUND, event.getSessionId());
                acknowledgment.acknowledge();
                return;
            }

            reservation.setStatus(ReservationStatus.COMPLETED);

            reservation.getTable().setStatus(TableStatus.OPEN);

            pushPendingReservations.pushPendingReservations();

            acknowledgment.acknowledge();
            log.info(AppConstants.LOG_MESSAGES.RESERVATION_COMPLETED, event.getSessionId());

        } catch (Exception e) {
            log.error(AppConstants.LOG_MESSAGES.INVOICE_EVENT_FAILED, event.getInvoiceId(), e);
            throw e;
        }

    }

}
