package com.amotassim.practitioner_service.controller;

import com.amotassim.practitioner_service.domain.Practitioner;
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
    public List<Practitioner> getAllPractitioners() {
        return practitioners;
    }

    @Operation(summary = "Obtenir un praticien par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable String id) {
        Optional<Practitioner> practitioner = practitioners.stream().filter(p -> p.getId().equals(id)).findFirst();
        return practitioner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter un nouveau praticien")
    @PostMapping
    public ResponseEntity<Practitioner> addPractitioner(@RequestBody Practitioner practitioner) {
        practitioner.setId(UUID.randomUUID().toString());
        practitioners.add(practitioner);
        return ResponseEntity.status(HttpStatus.CREATED).body(practitioner);
    }

    @Operation(summary = "Mettre à jour un praticien")
    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deletePractitioner(@PathVariable String id) {
        if (practitioners.removeIf(practitioner -> practitioner.getId().equals(id))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
