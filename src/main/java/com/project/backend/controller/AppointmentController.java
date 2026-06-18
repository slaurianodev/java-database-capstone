package com.project.backend.controller;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Appointment;
import com.project.backend.service.AppointmentService;
import com.project.backend.service.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final MainService service;

    public AppointmentController(AppointmentService appointmentService,
                                 MainService service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // =========================
    // GET APPOINTMENTS
    // =========================

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
                                             @PathVariable String patientName,
                                             @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        boolean validation = service.validateToken(token, "doctor");

        if (!validation) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        LocalDate parsedDate;

        try {
            // Expected format: yyyy-MM-dd (e.g. 2026-06-03)
            parsedDate = LocalDate.parse(date);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message",
                            "Invalid date format. Expected format: yyyy-MM-dd"));
        }

        return ResponseEntity.ok(
                appointmentService.getAppointment(patientName, parsedDate, token)
        );
    }

    // =========================
    // BOOK APPOINTMENT
    // =========================

    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(@PathVariable String token,
                                             @RequestBody Appointment appointment) {

        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>());
        }

        int result = service.validateAppointment(appointment);

        if (result == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Doctor not found"));
        }

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Appointment time not available"));
        }

        Appointment bookedAppointment = appointmentService.bookAppointment(appointment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Appointment booked successfully",
                        "appointmentId", String.valueOf(bookedAppointment.getId())
                ));
    }

    // =========================
    // UPDATE APPOINTMENT
    // =========================

    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(@PathVariable String token,
                                               @RequestBody Appointment appointment) {

        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>());
        }

        return ResponseEntity.ok(
                appointmentService.updateAppointment(appointment)
        );
    }

    // =========================
    // CANCEL APPOINTMENT
    // =========================

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id,
                                               @PathVariable String token) {


        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>());
        }

        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id, token)
        );
    }
}
