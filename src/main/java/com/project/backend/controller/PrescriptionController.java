package com.project.backend.controller;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Prescription;
import com.project.backend.service.PrescriptionService;
import com.project.backend.service.MainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private MainService service;

    /**
     * Save Prescription
     */
    @PostMapping("/{token}")
    public ResponseEntity<?> savePrescription(
            @PathVariable String token,
            @Valid @RequestBody Prescription prescription) {

        boolean validToken = service.validateToken(token, "doctor");

        if (!validToken) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token.");
        }

        prescriptionService.savePrescription(prescription);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Prescription saved successfully.");
    }

    /**
     * Get Prescription by Appointment ID
     */
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        boolean validToken = service.validateToken(token, "doctor");

        if (!validToken) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token.");
        }

        return prescriptionService.getPrescription(appointmentId);

    }
}
