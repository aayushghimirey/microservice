package com.sts.config;

import com.sts.filter.TenantHolder;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        String tenantId = request.getHeaders().getFirst("X-Tenant-Id");

        if (tenantId == null) {
            throw new RuntimeException("Missing tenantId");
        }

        // store in session attributes (IMPORTANT)
        attributes.put("tenantId", tenantId);

        return () -> tenantId;
    }
}