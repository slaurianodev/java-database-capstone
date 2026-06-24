package com.project.backend.service;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Appointment;
import com.project.backend.repository.AppointmentRepository;
import com.project.backend.repository.DoctorRepository;
import com.project.backend.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    // BOOK APPOINTMENT
    public Appointment bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return appointment;
        } catch (Exception e) {
            return null;
        }
    }

    // UPDATE APPOINTMENT
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existingOpt = appointmentRepository.findById(appointment.getId());

        if (existingOpt.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Basic validation hook (assumes exists in your service layer)
            validateAppointment(appointment);

            appointmentRepository.save(appointment);

            response.put("message", "Appointment updated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error updating appointment: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // CANCEL APPOINTMENT
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);

        if (appointmentOpt.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        Appointment appointment = appointmentOpt.get();

        try {
            Long userId = Long.valueOf(tokenService.extractIdentifier(token));

            // Ensure only the owner (patient) can cancel
            if (!appointment.getId().equals(userId)) {
                response.put("message", "Unauthorized to cancel this appointment");
                return ResponseEntity.status(403).body(response);
            }

            appointmentRepository.delete(appointment);

            response.put("message", "Appointment cancelled successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error cancelling appointment: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // GET APPOINTMENTS FOR DOCTOR ON DATE (OPTIONALLY FILTER BY PATIENT NAME)
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long doctorId = Long.valueOf(tokenService.extractIdentifier(token));

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);

            List<Appointment> appointments;

            if (pname != null && !pname.isEmpty()) {
                appointments = appointmentRepository
                        .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                                doctorId, pname, start, end
                        );
            } else {
                appointments = appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
            }

            response.put("appointments", appointments);
            response.put("count", appointments.size());

            return response;

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return response;
        }
    }

    private void validateAppointment(Appointment appointment) {

        if (appointment == null
                || appointment.getDoctor() == null
                || appointment.getPatient() == null
                || appointment.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Appointment, doctor, patient, and time are required");
        }

        if (appointment.getAppointmentTime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment time cannot be in the past");
        }
    }
}
