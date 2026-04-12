package com.sts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@SpringBootApplication
public class ApiGatewayApplication {

    @Autowired
    private RequestNavigator requestNavigator;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", predicateSpec -> predicateSpec
                        .path("/auth/public/**", "/auth/super/**")
                        .uri("lb://auth-service")
                )
                .route("inventory-service", predicateSpec -> predicateSpec
                        .path("/stocks/**", "/purchases/**", "/vendors/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://inventory-service"))
                .route("finance-service", predicateSpec -> predicateSpec
                        .path("/finances/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))

                        .uri("lb://finance-service"))
                .route("menu-service", predicateSpec -> predicateSpec
                        .path("/menus/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))

                        .uri("lb://menu-service"))
                .route("order-service", predicateSpec -> predicateSpec
                        .path("/tables/**", "/reservations/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))

                        .uri("lb://order-service"))
                .route("invoice-service", predicateSpec -> predicateSpec
                        .path("/invoices/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://invoice-service"))

                .route("order-service-ws", r -> r
                        .path("/orders/ws/**")
                        .filters(f -> f.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://order-service")
                )

                .route("invoice-service-ws", r -> r
                        .path("/invoices/ws/**")
                        .filters(f -> f.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://invoice-service")
                )
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:7000",
                "http://localhost:3000",
                "http://localhost:5173"
        ));
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Upgrade",                  // 👈 required for WebSocket
                "Connection",               // 👈 required for WebSocket
                "Sec-WebSocket-Key",        // 👈 required for WebSocket
                "Sec-WebSocket-Version",    // 👈 required for WebSocket
                "Sec-WebSocket-Extensions", // 👈 required for WebSocket
                "Sec-WebSocket-Protocol"    // 👈 required for WebSocket
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

}
