package com.amotassim.patient_service.controller;

import com.amotassim.patient_service.domain.Patient;
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
@RequestMapping("/patients")
@Tag(name = "Patient Controller", description = "CRUD pour la gestion des patients")
public class PatientController {
    private final List<Patient> patients = new ArrayList<>(Arrays.asList(
            new Patient(UUID.randomUUID().toString(), "Alice Dupont", "30"),
            new Patient(UUID.randomUUID().toString(), "Jean Martin", "45"),
            new Patient(UUID.randomUUID().toString(), "Sophie Durant", "29")));

    @Operation(summary = "Obtenir la liste des patients")
    @GetMapping
    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackGetAllPatients")
    @RateLimiter(name = "patientService")
    public List<Patient> getAllPatients() {
        return patients;
    }

    @Operation(summary = "Obtenir un patient par ID")
    @GetMapping("/{id}")
    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackGetPatientById")
    @RateLimiter(name = "patientService")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        Optional<Patient> patient = patients.stream().filter(p -> p.getId().equals(id)).findFirst();
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter un nouveau patient")
    @PostMapping
    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackAddPatient")
    @RateLimiter(name = "patientService")
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        patient.setId(UUID.randomUUID().toString());
        patients.add(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @Operation(summary = "Mettre à jour un patient")
    @PutMapping("/{id}")
    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackUpdatePatient")
    @RateLimiter(name = "patientService")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId().equals(id)) {
                updatedPatient.setId(id);
                patients.set(i, updatedPatient);
                return ResponseEntity.ok(updatedPatient);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Supprimer un patient")
    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackDeletePatient")
    @RateLimiter(name = "patientService")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        if (patients.removeIf(patient -> patient.getId().equals(id))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Fallback methods
    public List<Patient> fallbackGetAllPatients(Exception e) {
        return Arrays.asList(new Patient("0", "Fallback Patient", "Unknown"));
    }

    public ResponseEntity<Patient> fallbackGetPatientById(String id, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Patient(id, "Fallback Patient", "Unknown"));
    }

    public ResponseEntity<Patient> fallbackAddPatient(Patient patient, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Patient("0", "Fallback Patient", "Unknown"));
    }

    public ResponseEntity<Patient> fallbackUpdatePatient(String id, Patient updatedPatient, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Patient(id, "Fallback Patient", "Unknown"));
    }

    public ResponseEntity<Void> fallbackDeletePatient(String id, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
