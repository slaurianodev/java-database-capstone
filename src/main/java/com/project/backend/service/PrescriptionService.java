package com.project.backend.service;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Prescription;
import com.project.backend.repository.PrescriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    // =========================
    // SAVE PRESCRIPTION
    // =========================

    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {

        Map<String, String> response = new HashMap<>();

        try {
            prescriptionRepository.save(prescription);

            response.put("message", "Prescription saved");
            return ResponseEntity.status(201).body(response);

        } catch (Exception e) {
            response.put("message", "Error saving prescription");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // =========================
    // GET PRESCRIPTION BY APPOINTMENT ID
    // =========================

    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Prescription> prescriptions =
                    prescriptionRepository.findByAppointmentId(appointmentId);

            response.put("prescriptions", prescriptions);
            response.put("count", prescriptions.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error fetching prescription");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
