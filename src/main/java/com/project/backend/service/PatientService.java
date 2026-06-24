package com.project.backend.service;

/**
 * Created by Sergio.
 */
import com.project.backend.dto.AppointmentDTO;
import com.project.backend.models.Appointment;
import com.project.backend.models.Patient;
import com.project.backend.repository.AppointmentRepository;
import com.project.backend.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // =========================
    // CREATE PATIENT
    // =========================

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // =========================
    // GET PATIENT APPOINTMENTS
    // =========================

    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            String email = tokenService.extractIdentifier(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null || !patient.getId().equals(id)) {
                response.put("message", "Unauthorized");
                return ResponseEntity.status(403).body(response);
            }

            List<Appointment> appointments = appointmentRepository.findByPatientId(id);

            List<AppointmentDTO> dtoList = appointments.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            response.put("appointments", dtoList);
            response.put("count", dtoList.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error fetching appointments");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // =========================
    // FILTER BY CONDITION
    // =========================

    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            int status = condition.equalsIgnoreCase("past") ? 1 : 0;

            List<Appointment> appointments =
                    appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status);

            response.put("appointments", appointments);
            response.put("count", appointments.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error filtering appointments");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // =========================
    // FILTER BY DOCTOR NAME
    // =========================

    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Appointment> appointments =
                    appointmentRepository.filterByDoctorNameAndPatientId(name, patientId);

            response.put("appointments", appointments);
            response.put("count", appointments.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error filtering by doctor");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // =========================
    // FILTER BY DOCTOR + CONDITION
    // =========================

    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition,
                                                                          String name,
                                                                          long patientId) {

        Map<String, Object> response = new HashMap<>();

        try {
            int status = condition.equalsIgnoreCase("past") ? 1 : 0;

            List<Appointment> appointments =
                    appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(name, patientId, status);

            response.put("appointments", appointments);
            response.put("count", appointments.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error filtering appointments");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // =========================
    // GET PATIENT DETAILS
    // =========================

    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {

        Map<String, Object> response = new HashMap<>();

        try {
            String email = tokenService.extractIdentifier(token);

            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                response.put("message", "Patient not found");
                return ResponseEntity.badRequest().body(response);
            }

            response.put("patient", patient);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error fetching patient details");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // =========================
    // DTO MAPPER
    // =========================

    private AppointmentDTO convertToDTO(Appointment a) {
        return new AppointmentDTO(
                a.getId(),
                a.getDoctor().getId(),
                a.getDoctor().getName(),
                a.getPatient().getId(),
                a.getPatient().getName(),
                a.getPatient().getEmail(),
                a.getPatient().getPhone(),
                a.getPatient().getAddress(),
                a.getAppointmentTime(),
                a.getStatus()
        );
    }
}
