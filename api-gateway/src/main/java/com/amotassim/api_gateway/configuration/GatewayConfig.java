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
                                // Patient Service Routes with Circuit Breaker and Rate Limiter
                                .route("patient_service", r -> r.path("/patients/**")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("patientService")
                                                                                .setFallbackUri("forward:/fallback/patients")))
                                                .uri("lb://PATIENT-SERVICE"))
                                .route("patient_by_id", r -> r.path("/patients/{id}")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("patientService")
                                                                                .setFallbackUri("forward:/fallback/patients")))
                                                .uri("lb://PATIENT-SERVICE"))
                                .route("add_patient", r -> r.path("/patients")
                                                .and().method("POST")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("patientService")
                                                                                .setFallbackUri("forward:/fallback/patients")))
                                                .uri("lb://PATIENT-SERVICE"))
                                .route("update_patient", r -> r.path("/patients/{id}")
                                                .and().method("PUT")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("patientService")
                                                                                .setFallbackUri("forward:/fallback/patients")))
                                                .uri("lb://PATIENT-SERVICE"))
                                .route("delete_patient", r -> r.path("/patients/{id}")
                                                .and().method("DELETE")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("patientService")
                                                                                .setFallbackUri("forward:/fallback/patients")))
                                                .uri("lb://PATIENT-SERVICE"))

                                // Practitioner Service Routes with Circuit Breaker and Rate Limiter
                                .route("practitioner_service", r -> r.path("/practitioner/**")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("practitionerService")
                                                                                .setFallbackUri("forward:/fallback/practitioners")))
                                                .uri("lb://PRACTITIONER-SERVICE"))
                                .route("practitioner_by_id", r -> r.path("/practitioner/{id}")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("practitionerService")
                                                                                .setFallbackUri("forward:/fallback/practitioners")))
                                                .uri("lb://PRACTITIONER-SERVICE"))
                                .route("add_practitioner", r -> r.path("/practitioner")
                                                .and().method("POST")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("practitionerService")
                                                                                .setFallbackUri("forward:/fallback/practitioners")))
                                                .uri("lb://PRACTITIONER-SERVICE"))
                                .route("update_practitioner", r -> r.path("/practitioner/{id}")
                                                .and().method("PUT")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("practitionerService")
                                                                                .setFallbackUri("forward:/fallback/practitioners")))
                                                .uri("lb://PRACTITIONER-SERVICE"))
                                .route("delete_practitioner", r -> r.path("/practitioner/{id}")
                                                .and().method("DELETE")
                                                .filters(f -> f
                                                                .circuitBreaker(c -> c.setName("practitionerService")
                                                                                .setFallbackUri("forward:/fallback/practitioners")))
                                                .uri("lb://PRACTITIONER-SERVICE"))
                                .build();
        }
}
