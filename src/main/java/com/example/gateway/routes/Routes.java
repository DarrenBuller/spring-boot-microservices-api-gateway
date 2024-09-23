package com.example.gateway.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration(proxyBeanMethods = false)
public class Routes {
        @Bean
        public RouterFunction<ServerResponse> productServiceRoute() {
                return route("product_service")
                                .route(RequestPredicates.path("/api/product"), http("http://product:8080"))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> orderServiceRoute() {
                return route("order_service")
                                .route(RequestPredicates.path("/api/order"), http("http://order:8081"))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> inventoryServiceRoute() {
                return route("inventory_service")
                                .route(RequestPredicates.path("/api/inventory"), http("http://inventory:8082"))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
                return route("product_service_swagger")
                                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                                                HandlerFunctions.http("http://product:8080"))
                                .filter(setPath("/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
                return route("order_service_swagger")
                                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                                                HandlerFunctions.http("http://order:8081"))
                                .filter(setPath("/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
                return route("inventory_service_swagger")
                                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                                                HandlerFunctions.http("http://inventory:8082"))
                                .filter(setPath("/api-docs"))
                                .build();
        }
}
