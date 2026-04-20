package com.sts.config.filter;

import com.sts.filter.TenantHolder;
import com.sts.service.JwtService;
import com.sts.utils.AppConstants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    private final String SUPER_ADMIN_USERNAME = "superadmin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // public urls
        if (pathMatcher.match("/auth/public/**", request.getServletPath()) ||
                pathMatcher.match("/auth/super/login", request.getServletPath())) {
            log.info("Skipping JWT filter: Match found for {}", request.getServletPath());
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        log.info("Token found: {}", token);

        try {
            final String username = jwtService.extractUsername(token);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails;

                if (SUPER_ADMIN_USERNAME.equals(username)) {
                    log.info("Static SuperAdmin detected, bypassing database lookup");
                    // Create a dummy UserDetails object for the static admin
                    userDetails = org.springframework.security.core.userdetails.User.builder()
                            .username(SUPER_ADMIN_USERNAME)
                            .password("") // Password doesn't matter for token validation
                            .roles("SUPER_ADMIN") // This adds the ROLE_ prefix automatically
                            .build();
                } else {
                    // Regular user: load from database via UserDetailsService
                    userDetails = userDetailsService.loadUserByUsername(username);
                }

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    TenantHolder.setTenantId(UUID.fromString(jwtService.extractTenantId(token)));

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.error("Error logging in: {}", exception.getMessage());
            // Optionally send a 401 response here instead of just continuing the chain
            filterChain.doFilter(request, response);
            TenantHolder.clear();
        }
    }
}