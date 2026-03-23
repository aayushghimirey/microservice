package com.sts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

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
                .route("inventory-service", predicateSpec -> predicateSpec
                        .path("/stocks/**", "/purchases/**", "/vendors/**")
                        .filters(filterSpec -> filterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://inventory-service"))
                .route("finance-service", predicateSpec -> predicateSpec
                        .path("/finances/**")
                        .filters(filterSpec -> filterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://finance-service"))
                .route("menu-service", predicateSpec -> predicateSpec
                        .path("/menus/**")
                        .filters(filterSpec -> filterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://menu-service"))
                .route("order-service", predicateSpec -> predicateSpec
                        .path("/tables/**", "/reservations/**")
                        .filters(filterSpec -> filterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://order-service"))
                .route("invoice-service", predicateSpec -> predicateSpec
                        .path("/invoices/**")
                        .filters(filterSpec -> filterSpec.filter(requestNavigator.apply(new RequestNavigator.Config())))
                        .uri("lb://invoice-service"))
                .route("auth-service", predicateSpec -> predicateSpec
                        .path("/auth/public/**", "/auth/super/**")
                        .uri("lb://auth-service")
                )

                .build();
    }


}
