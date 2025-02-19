package com.amotassim.practitioner_service.controller;

import com.amotassim.practitioner_service.domain.Practitioner;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/practitioner")
@Tag(name = "Practitioner Controller", description = "CRUD pour la gestion des praticiens")
public class PractitionerController {
    private final List<Practitioner> practitioners = new ArrayList<>(Arrays.asList(
            new Practitioner(UUID.randomUUID().toString(), "Dr. Alice Dupont",
                    "Cardiologue"),
            new Practitioner(UUID.randomUUID().toString(), "Dr. Jean Martin",
                    "Dermatologue"),
            new Practitioner(UUID.randomUUID().toString(), "Dr. Sophie Durant",
                    "Pédiatre")));

    @Operation(summary = "Obtenir la liste des praticiens")
    @GetMapping
    @CircuitBreaker(name = "practitionerService", fallbackMethod = "fallbackGetAllPractitioners")
    @RateLimiter(name = "practitionerService")
    public List<Practitioner> getAllPractitioners() {
        return practitioners;
    }

    @Operation(summary = "Obtenir un praticien par ID")
    @GetMapping("/{id}")
    @CircuitBreaker(name = "practitionerService", fallbackMethod = "fallbackGetPractitionerById")
    @RateLimiter(name = "practitionerService")
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable String id) {
        Optional<Practitioner> practitioner = practitioners.stream().filter(p -> p.getId().equals(id)).findFirst();
        return practitioner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter un nouveau praticien")
    @PostMapping
    @CircuitBreaker(name = "practitionerService", fallbackMethod = "fallbackAddPractitioner")
    @RateLimiter(name = "practitionerService")
    public ResponseEntity<Practitioner> addPractitioner(@RequestBody Practitioner practitioner) {
        practitioner.setId(UUID.randomUUID().toString());
        practitioners.add(practitioner);
        return ResponseEntity.status(HttpStatus.CREATED).body(practitioner);
    }

    @Operation(summary = "Mettre à jour un praticien")
    @PutMapping("/{id}")
    @CircuitBreaker(name = "practitionerService", fallbackMethod = "fallbackUpdatePractitioner")
    @RateLimiter(name = "practitionerService")
    public ResponseEntity<Practitioner> updatePractitioner(@PathVariable String id,
            @RequestBody Practitioner updatedPractitioner) {
        for (int i = 0; i < practitioners.size(); i++) {
            if (practitioners.get(i).getId().equals(id)) {
                updatedPractitioner.setId(id);
                practitioners.set(i, updatedPractitioner);
                return ResponseEntity.ok(updatedPractitioner);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Supprimer un praticien")
    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "practitionerService", fallbackMethod = "fallbackDeletePractitioner")
    @RateLimiter(name = "practitionerService")
    public ResponseEntity<Void> deletePractitioner(@PathVariable String id) {
        if (practitioners.removeIf(practitioner -> practitioner.getId().equals(id))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Fallback methods
    public List<Practitioner> fallbackGetAllPractitioners(Exception e) {
        return Arrays.asList(new Practitioner("0", "Fallback Practitioner", "Unknown"));
    }

    public ResponseEntity<Practitioner> fallbackGetPractitionerById(String id, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Practitioner(id, "Fallback Practitioner", "Unknown"));
    }

    public ResponseEntity<Practitioner> fallbackAddPractitioner(Practitioner practitioner, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Practitioner("0", "Fallback Practitioner", "Unknown"));
    }

    public ResponseEntity<Practitioner> fallbackUpdatePractitioner(String id, Practitioner updatedPractitioner,
            Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Practitioner(id, "Fallback Practitioner", "Unknown"));
    }

    public ResponseEntity<Void> fallbackDeletePractitioner(String id, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
