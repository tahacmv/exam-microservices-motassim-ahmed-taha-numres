package com.amotassim.api_gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient_service", r -> r.path("/patients/**")
                        .uri("lb://PATIENT-SERVICE"))
                .route("patient_by_id", r -> r.path("/patients/{id}")
                        .uri("lb://PATIENT-SERVICE"))
                .route("add_patient", r -> r.path("/patients")
                        .and().method("POST")
                        .uri("lb://PATIENT-SERVICE"))
                .route("update_patient", r -> r.path("/patients/{id}")
                        .and().method("PUT")
                        .uri("lb://PATIENT-SERVICE"))
                .route("delete_patient", r -> r.path("/patients/{id}")
                        .and().method("DELETE")
                        .uri("lb://PATIENT-SERVICE"))
                .route("practitioner_service", r -> r.path("/practitioner/**")
                        .uri("lb://PRACTITIONER-SERVICE"))
                .route("practitioner_by_id", r -> r.path("/practitioner/{id}")
                        .uri("lb://PRACTITIONER-SERVICE"))
                .route("add_practitioner", r -> r.path("/practitioner")
                        .and().method("POST")
                        .uri("lb://PRACTITIONER-SERVICE"))
                .route("update_practitioner", r -> r.path("/practitioner/{id}")
                        .and().method("PUT")
                        .uri("lb://PRACTITIONER-SERVICE"))
                .route("delete_practitioner", r -> r.path("/practitioner/{id}")
                        .and().method("DELETE")
                        .uri("lb://PRACTITIONER-SERVICE"))
                .build();
    }
}