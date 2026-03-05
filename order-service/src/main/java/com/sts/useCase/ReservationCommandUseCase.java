package com.sts.useCase;

import com.sts.command.CreateReservationCommand;
import com.sts.command.ReservationCommandHandler;
import com.sts.domain.model.Reservation;
import com.sts.domain.model.Table;
import com.sts.repository.ReservationRepository;
import com.sts.repository.TableRepository;
import com.sts.entity.OutboxEvent;
import com.sts.entity.OutboxEventType;
import com.sts.event.OrderCreatedEvent;
import com.sts.mapper.OutboxMapper;
import com.sts.repository.OutboxEventRepository;
import com.sts.topics.KafkaTopics;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class ReservationCommandUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationCommandHandler reservationCommandHandler;
    private final TableRepository tableRepository;
    private final OutboxMapper outboxMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTopics kafkaTopics;

    @Transactional
    public Reservation createReservation(CreateReservationCommand command) {

        Table table = tableRepository.findById(command.tableId()).orElseThrow(
                () -> new IllegalArgumentException("Table not found with id: " + command.tableId())
        );

        Reservation reservation = reservationCommandHandler.buildReservation(command, table);

        publishOrderCreatedEvent(reservation);

        return reservationRepository.save(reservation);
    }

    private void publishOrderCreatedEvent(Reservation reservation) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setReservationId(reservation.getId());
        orderCreatedEvent.setSessionId(reservation.getSessionId());
        orderCreatedEvent.setStatus(reservation.getStatus().name());
        orderCreatedEvent.setTableId(reservation.getTable().getId());
        orderCreatedEvent.setBillAmount(reservation.getBillAmount());
        orderCreatedEvent.setReservationTime(reservation.getReservationTime());

        String payload = objectMapper.writeValueAsString(orderCreatedEvent);

        OutboxEvent outboxEvent = outboxMapper.map(
                "ORDER",
                reservation.getId().toString(),
                OutboxEventType.CREATED,
                payload,
                kafkaTopics.getOrderEvent()
        );

        outboxEventRepository.save(outboxEvent);

    }

}
