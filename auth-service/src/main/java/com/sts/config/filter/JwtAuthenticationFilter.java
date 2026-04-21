package com.sts.config.filter;

import com.sts.filter.TenantHolder;
import com.sts.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final String SUPER_ADMIN_USERNAME = "superadmin";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip public endpoints
        if (pathMatcher.match("/auth/public/**", path) ||
                pathMatcher.match("/auth/super/login", path)) {

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();

        if (token.isBlank()) {
            log.error("Empty JWT token");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtService.extractUsername(token);

            if (username == null || username.isBlank()) {
                log.error("JWT missing username/subject");
                filterChain.doFilter(request, response);
                return;
            }

            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

            if (existingAuth == null) {

                UserDetails userDetails;

                if (SUPER_ADMIN_USERNAME.equals(username)) {
                    log.info("Static SuperAdmin detected");

                    userDetails = org.springframework.security.core.userdetails.User.builder()
                            .username(SUPER_ADMIN_USERNAME)
                            .password("")
                            .roles("SUPER_ADMIN")
                            .build();
                } else {
                    userDetails = userDetailsService.loadUserByUsername(username);
                }

                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    String tenantId = jwtService.extractTenantId(token);

                    if (tenantId != null && !tenantId.isBlank()) {
                        TenantHolder.setTenantId(UUID.fromString(tenantId));
                    }

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("JWT processing failed: {}", ex.getMessage(), ex);

            SecurityContextHolder.clearContext();
            TenantHolder.clear();

            // optional: return 401 instead of continuing
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}