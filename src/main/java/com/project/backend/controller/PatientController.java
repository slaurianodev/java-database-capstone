package com.project.backend.controller;

/**
 * Created by Sergio.
 */
import com.project.backend.dto.LoginDTO;
import com.project.backend.models.Patient;
import com.project.backend.service.MainService;
import com.project.backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private MainService service;

    /**
     * 1. Get Patient Details
     */
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatientDetails(
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!service.validateToken(token, "patient")) {
                response.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            return ResponseEntity.ok(
                    patientService.getPatientDetails(token));

        } catch (Exception e) {
            response.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 2. Create a New Patient
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPatient(
            @RequestBody Patient patient) {

        Map<String, Object> response = new HashMap<>();

        try {
            int result = patientService.createPatient(patient);

            if (result == 1) {
                response.put("message", "Signup successful");

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(response);
            }

            response.put("message",
                    "Patient with email id or phone no already exist");

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(response);

        } catch (Exception e) {
            response.put("message", "Internal server error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 3. Patient Login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDTO login) {

        try {
            return ResponseEntity.ok(
                    service.validatePatientLogin(login));

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Internal server error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 4. Get Patient Appointments
     */
    @GetMapping("/{id}/{token}")
    public ResponseEntity<?> getPatientAppointments(
            @PathVariable Long id,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!service.validateToken(token, "patient")) {
                response.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            return ResponseEntity.ok(
                    patientService.getPatientAppointment(id, token));

        } catch (Exception e) {
            response.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 5. Filter Patient Appointments
     */
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!service.validateToken(token, "patient")) {
                response.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            return ResponseEntity.ok(
                    service.filterPatient(condition, name, token));

        } catch (Exception e) {
            response.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}