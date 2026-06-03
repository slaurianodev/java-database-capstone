package com.project.backend.service;

/**
 * Created by Sergio.
 */
import com.project.backend.dto.LoginDTO;
import com.project.backend.models.Admin;
import com.project.backend.models.Appointment;
import com.project.backend.models.Doctor;
import com.project.backend.models.Patient;
import com.project.backend.repository.AdminRepository;
import com.project.backend.repository.DoctorRepository;
import com.project.backend.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MainService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public MainService(TokenService tokenService,
                       AdminRepository adminRepository,
                       DoctorRepository doctorRepository,
                       PatientRepository patientRepository,
                       DoctorService doctorService,
                       PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // =========================
    // TOKEN VALIDATION
    // =========================

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {

        Map<String, String> response = new HashMap<>();

        if (!tokenService.validateToken(token, user)) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(401).body(response);
        }

        response.put("message", "Token is valid");
        return ResponseEntity.ok(response);
    }

    // =========================
    // ADMIN LOGIN
    // =========================

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {

        Map<String, String> response = new HashMap<>();

        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());

            if (admin == null || !admin.getPassword().equals(receivedAdmin.getPassword())) {
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(401).body(response);
            }

            String token = tokenService.generateToken(admin.getId(), "ADMIN");

            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error during login");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // =========================
    // FILTER DOCTOR
    // =========================

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {

        if (name != null && specialty != null && time != null) {
            return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
        }

        if (name != null && specialty != null) {
            return doctorService.filterDoctorByNameAndSpecility(name, specialty);
        }

        if (name != null && time != null) {
            return doctorService.filterDoctorByNameAndTime(name, time);
        }

        if (specialty != null && time != null) {
            return doctorService.filterDoctorByTimeAndSpecility(specialty, time);
        }

        if (name != null) {
            return doctorService.findDoctorByName(name);
        }

        if (specialty != null) {
            return doctorService.filterDoctorBySpecility(specialty);
        }

        if (time != null) {
            return doctorService.filterDoctorsByTime(time);
        }

        return Map.of("doctors", doctorService.getDoctors());
    }

    // =========================
    // VALIDATE APPOINTMENT
    // =========================

    public int validateAppointment(Appointment appointment) {

        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());

        if (doctorOpt.isEmpty()) {
            return -1;
        }

        List<String> availableSlots = doctorService.getDoctorAvailability(
                appointment.getDoctor().getId(),
                appointment.getAppointmentTime().toLocalDate()
        );

        String requestedTime =
                appointment.getAppointmentTime().toLocalTime().toString().substring(0, 5);

        if (availableSlots.contains(requestedTime)) {
            return 1;
        }

        return 0;
    }

    // =========================
    // VALIDATE PATIENT (REGISTER)
    // =========================

    public boolean validatePatient(Patient patient) {

        Patient existing =
                patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());

        return existing == null;
    }

    // =========================
    // PATIENT LOGIN
    // =========================

    public ResponseEntity<Map<String, String>> validatePatientLogin(LoginDTO login) {

        Map<String, String> response = new HashMap<>();

        try {
            Patient patient = patientRepository.findByEmail(login.getIdentifier());

            if (patient == null || !patient.getPassword().equals(login.getPassword())) {
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(401).body(response);
            }

            String token = tokenService.generateToken(patient.getId(), "PATIENT");

            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error during login");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // =========================
    // FILTER PATIENT APPOINTMENTS
    // =========================

    public ResponseEntity<Map<String, Object>> filterPatient(String condition,
                                                             String name,
                                                             String token) {

        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
            }

            Long patientId = patient.getId();

            if (condition != null && name != null) {
                return patientService.filterByDoctorAndCondition(condition, name, patientId);
            }

            if (condition != null) {
                return patientService.filterByCondition(condition, patientId);
            }

            if (name != null) {
                return patientService.filterByDoctor(name, patientId);
            }

            return patientService.getPatientAppointment(patientId, token);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error filtering appointments"));
        }
    }
}
