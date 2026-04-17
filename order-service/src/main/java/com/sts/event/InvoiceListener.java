package com.sts.event;

import com.sts.dto.response.ReservationResponse;
import com.sts.filter.TenantHolder;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sts.mapper.ReservationMapper;
import com.sts.model.Reservation;
import com.sts.repository.ReservationRepository;
import com.sts.topics.KafkaProperties;
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
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RlsContext rlsContext;


    @KafkaListener(topics = "${app.kafka.topics.invoice-event}", containerFactory = "invoiceKafkaListenerContainerFactory")
    @Transactional
    public void listen(InvoiceEvent event, Acknowledgment acknowledgment) {
        log.info(AppConstants.LOG_MESSAGES.INVOICE_EVENT_RECEIVED, event.getInvoiceId());

        TenantHolder.setTenantId(event.getTenantId());
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();


        try {
            Reservation reservation = reservationRepository.findBySessionId(event.getSessionId());

            if (reservation == null) {
                log.warn(AppConstants.LOG_MESSAGES.SESSION_NOT_FOUND, event.getSessionId());
                acknowledgment.acknowledge();
                return;
            }

            reservation.setStatus(ReservationStatus.COMPLETED);
            reservation.getTable().setStatus(TableStatus.OPEN);
            reservation = reservationRepository.save(reservation);

            acknowledgment.acknowledge();

            log.info(AppConstants.LOG_MESSAGES.RESERVATION_COMPLETED, event.getSessionId());

            ReservationResponse response = reservationMapper.toResponse(reservation);

            simpMessagingTemplate.convertAndSendToUser(reservation.getTenantId().toString(), "/queue/orders", response);


        } catch (Exception e) {
            acknowledgment.acknowledge();
            log.error(AppConstants.LOG_MESSAGES.INVOICE_EVENT_FAILED, event.getInvoiceId(), e);
            throw e;
        }

    }

}
