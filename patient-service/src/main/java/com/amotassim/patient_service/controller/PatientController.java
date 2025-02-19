package com.amotassim.patient_service.controller;

import com.amotassim.patient_service.domain.Patient;
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
    public List<Patient> getAllPatients() {
        return patients;
    }

    @Operation(summary = "Obtenir un patient par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        Optional<Patient> patient = patients.stream().filter(p -> p.getId().equals(id)).findFirst();
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter un nouveau patient")
    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        patient.setId(UUID.randomUUID().toString());
        patients.add(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @Operation(summary = "Mettre à jour un patient")
    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        if (patients.removeIf(patient -> patient.getId().equals(id))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
