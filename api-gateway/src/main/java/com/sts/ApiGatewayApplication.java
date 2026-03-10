package com.sts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    private static final Logger log = LoggerFactory.getLogger(ApiGatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("inventory-service", predicateSpec -> predicateSpec
                        .path("/stocks/**", "/purchases/**", "/vendors/**")
                        .uri("lb://inventory-service")
                )
                .route("finance-service", predicateSpec -> predicateSpec
                        .path("/finance/**")
                        .uri("lb://finance-service")
                ).route("menu-service", predicateSpec -> predicateSpec
                        .path("/menus/**")
                        .uri("lb://menu-service")
                ).route("order-service", predicateSpec -> predicateSpec
                        .path("/tables/**", "/reservations/**")
                        .uri("lb://order-service")
                ).route("invoice-service", predicateSpec -> predicateSpec
                        .path("/invoices/**")
                        .uri("lb://invoice-service")
                )

                .build();
    }


}
