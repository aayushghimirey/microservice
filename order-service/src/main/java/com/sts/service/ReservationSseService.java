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

    public void sendUpdate(UUID tenantId, Object data) {

        SseEmitter emitter = emitters.get(tenantId);

        if (emitter == null) {
            return;
        }

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("pending-orders")
                            .data(data)
            );
        } catch (Exception e) {
            emitters.remove(tenantId);
        }
    }

}