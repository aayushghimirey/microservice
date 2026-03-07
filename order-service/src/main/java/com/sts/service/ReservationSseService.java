package com.sts.service;

import com.sts.pagination.PageRequestDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ReservationSseService {

    private final ReservationService reservationService;

    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();


    public SseEmitter createEmitter(UUID tenantId) {

        SseEmitter emitter = new SseEmitter(0L);

        emitters.put(tenantId, emitter);

        emitter.onCompletion(() -> emitters.remove(tenantId));
        emitter.onTimeout(() -> emitters.remove(tenantId));
        emitter.onError(e -> emitters.remove(tenantId));

        return emitter;
    }


    public void initPush(UUID tenantId) {

        SseEmitter emitter = emitters.get(tenantId);

        if (emitter == null) return;

        try {

            emitter.send(
                    SseEmitter.event()
                            .name("initial-orders")
                            .data(reservationService.getAllPendingOrders())
            );

        } catch (Exception e) {
            emitters.remove(tenantId);
        }
    }


    public void newOrderEvent(UUID tenantId, Object order) {

        SseEmitter emitter = emitters.get(tenantId);

        if (emitter == null) return;

        try {

            emitter.send(
                    SseEmitter.event()
                            .name("new-order")
                            .data(order)
            );

        } catch (Exception e) {
            emitters.remove(tenantId);
        }
    }
}