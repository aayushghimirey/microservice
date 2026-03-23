package com.sts;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;


@Component
public class RequestNavigator extends AbstractGatewayFilterFactory<RequestNavigator.Config> {

    private static final Logger log = LoggerFactory.getLogger(RequestNavigator.class);


    @Value("${app.security.gateway-secret}")
    private String gatewaySecret;
    @Value("${app.security.secret-key}")
    private String SECRET_KEY;

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {


            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();
            String token = null;


            List<String> headers = request.getHeaders().get("Authorization");
            if (headers == null || headers.isEmpty()) {
                return onError(exchange, "No Authorization header found", HttpStatus.UNAUTHORIZED);
            }
            if (headers.get(0).startsWith("Bearer ")) {
                token = headers.get(0).substring(7);
            }

            if (token != null && !token.isEmpty()) {
                Claims claims = Jwts.parser()
                        .verifyWith(getSignInKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String tenantId = claims.get("tenantId", String.class);

                if (tenantId == null || tenantId.isBlank()) {
                    log.error("Token parsed successfully but 'tenantId' claim is missing for path: {}", path);
                    return onError(exchange, "Tenant context missing in token", HttpStatus.UNAUTHORIZED);
                }

                log.info("Tenant identified: {} for path: {}", tenantId, path);
                log.info("Gateway secret being added to the request headers for path: {} , {}", path, gatewaySecret);
                ServerWebExchange mutatedExchange = exchange.mutate().request(r -> r
                                /*
                                 *  if either of them missing will fail the request
                                 * */.header("X-Tenant-Id", tenantId) // add tenant id
                                .header("X-Gateway-Secret", gatewaySecret) // add gateway secret
                        )


                        .build();

                return chain.filter(mutatedExchange);


            } else {
                return onError(exchange, "No Authorization header found", HttpStatus.UNAUTHORIZED);
            }
        });

    }

    public static class Config {
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        log.warn("Blocking request: {} - Status: {}", err, status);
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
