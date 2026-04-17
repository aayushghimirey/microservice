package com.sts.filter;

import com.sts.exception.InvalidRequestException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class SecurityCheckFilter extends OncePerRequestFilter {

    @Value("${app.security.gateway-secret}")
    private String expectedGatewaySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.info("Received request for path: {}", path);

        if (isWebSocketRequest(path)) {
            log.info("Skipping security for WebSocket request: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.info("Performing security check for request to {}", path);

            String tenantId = request.getHeader("X-Tenant-Id");
            String gatewaySecret = request.getHeader("X-Gateway-Secret");

            if (tenantId == null || gatewaySecret == null) {
                log.warn("Missing tenantId or gatewaySecret");
                throw new InvalidRequestException("Invalid Request");
            }

            if (!expectedGatewaySecret.equals(gatewaySecret)) {
                log.warn("Invalid gateway secret: {}", gatewaySecret);
                throw new InvalidRequestException("Invalid gateway secret");
            }

            TenantHolder.setTenantId(UUID.fromString(tenantId));

            filterChain.doFilter(request, response);

        } finally {
            TenantHolder.clear();
        }
    }

    private boolean isWebSocketRequest(String path) {
        return path.matches(".*/ws(/.*)?");
    }
}