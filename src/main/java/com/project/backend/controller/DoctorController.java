package com.project.backend.controller;

/**
 * Created by Sergio.
 */
import com.project.backend.dto.LoginDTO;
import com.project.backend.models.Doctor;
import com.project.backend.service.DoctorService;
import com.project.backend.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private MainService service;

    /**
     * 1. Get Doctor Availability
     */
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!service.validateToken(token, user)) {
                response.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            LocalDate appointmentDate;
            try {
                appointmentDate = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                response.put("message", "Invalid date format. Expected yyyy-MM-dd");

                return ResponseEntity.badRequest().body(response);
            }
            response.put("availability",
                    doctorService.getDoctorAvailability(doctorId, appointmentDate));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Some internal error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 2. Get List of Doctors
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {

        Map<String, Object> response = new HashMap<>();

        try {
            response.put("doctors", doctorService.getDoctors());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Some internal error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 3. Add New Doctor
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> addDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!service.validateToken(token, "admin")) {
                response.put("message", "Invalid token");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            int result = doctorService.saveDoctor(doctor);

            if (result == 1) {
                response.put("message", "Doctor added to db");

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(response);
            }

            response.put("message", "Doctor already exists");

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(response);

        } catch (Exception e) {
            response.put("message", "Some internal error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 4. Doctor Login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        try {

            return ResponseEntity.ok(doctorService.validateDoctor(loginDTO));

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Some internal error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 5. Update Doctor Details
     */
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!service.validateToken(token, "admin")) {
                response.put("message", "Invalid token");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            int updated = doctorService.updateDoctor(doctor);

            if (updated == 1) {
                response.put("message", "Doctor updated");

                return ResponseEntity.ok(response);
            }

            response.put("message", "Doctor not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response);

        } catch (Exception e) {
            response.put("message", "Some internal error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 6. Delete Doctor
     */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(
            @PathVariable Long id,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!service.validateToken(token, "admin")) {
                response.put("message", "Invalid token");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            int deleted = doctorService.deleteDoctor(id);

            if (deleted == 1) {
                response.put("message", "Doctor deleted successfully");

                return ResponseEntity.ok(response);
            }

            response.put("message", "Doctor not found with id");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response);

        } catch (Exception e) {
            response.put("message", "Some internal error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * 7. Filter Doctors
     */
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        Map<String, Object> response = new HashMap<>();

        try {
            response.put("doctors",
                    service.filterDoctor(name, time, speciality));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Some internal error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
