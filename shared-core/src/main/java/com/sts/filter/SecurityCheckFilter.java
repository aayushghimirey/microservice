package com.sts.filter;

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

        try {

            String tenantId = request.getHeader("X-Tenant-Id");
            String gatewaySecret = request.getHeader("X-Gateway-Secret");

            if (tenantId == null || gatewaySecret == null) {
                log.warn("Security Failed. Missing tenant or gatewaySecret");
                throw new RuntimeException("Invalid Request");
            }

            if (!expectedGatewaySecret.equals(gatewaySecret)) {
                log.warn("Invalid gateway secret : {}", gatewaySecret);
                throw new RuntimeException("Invalid gateway secret");
            }

            TenantHolder.setTenantId(UUID.fromString(tenantId));

            // IMPORTANT: continue filter chain
            filterChain.doFilter(request, response);

        } finally {
            // ALWAYS clear
            TenantHolder.clear();
        }
    }
}
