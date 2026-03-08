package com.sts.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Component
@Slf4j
public class ReservationSseService {

    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(UUID tenantId) {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.put(tenantId, emitter);

        emitter.onCompletion(() -> emitters.remove(tenantId));
        emitter.onTimeout(() -> emitters.remove(tenantId));
        emitter.onError(e -> emitters.remove(tenantId));

        return emitter;
    }

    public void initPush(UUID tenantId, Object data) {

        SseEmitter emitter = emitters.get(tenantId);

        if (emitter == null) return;

        try {

            emitter.send(
                    SseEmitter.event()
                            .name("initial-orders")
                            .data(data)
            );

        } catch (Exception e) {
            emitters.remove(tenantId);
        }
    }

    public void newOrderEvent(UUID tenantId, Object order) {

        log.info("Create new order event push ready ");

        SseEmitter emitter = emitters.get(tenantId);


        if (emitter == null) {
            log.warn("No emitter found for tenant {}", tenantId);
            return;
        }
        try {
            log.info("Pushing SSE event to {}", tenantId);

            emitter.send(
                    SseEmitter.event()
                            .name("new-order")
                            .data(order)
            );

        } catch (Exception e) {
            log.error(e.getMessage(), "Error happen");
            emitters.remove(tenantId);
        }
    }
}