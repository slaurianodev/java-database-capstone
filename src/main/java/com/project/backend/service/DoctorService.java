package com.project.backend.service;

/**
 * Created by Sergio.
 */
import com.project.backend.dto.LoginDTO;
import com.project.backend.models.Appointment;
import com.project.backend.models.Doctor;
import com.project.backend.repository.AppointmentRepository;
import com.project.backend.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;
    private final PatientService patientService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService,
                         PatientService patientService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
        this.patientService = patientService;
    }

    // =========================
    // CRUD
    // =========================

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findById(doctor.getId()).isEmpty()) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(long id) {
        try {
            if (doctorRepository.findById(id).isEmpty()) {
                return -1;
            }

            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);

            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    // =========================
    // AUTH
    // =========================

    public ResponseEntity<Map<String, String>> validateDoctor(LoginDTO login) {

        Map<String, String> response = new HashMap<>();

        try {
            Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());

            if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
                response.put("message", "Invalid credentials");
                return ResponseEntity.badRequest().body(response);
            }

            String token = tokenService.generateToken(doctor.getId(), "DOCTOR");

            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error during login");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // =========================
    // SEARCH
    // =========================

    public Map<String, Object> findDoctorByName(String name) {
        return Map.of("doctors", doctorRepository.findByNameLike(name));
    }

    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        return Map.of("doctors",
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty));
    }

    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        return Map.of("doctors", doctorRepository.findBySpecialtyIgnoreCase(specialty));
    }

    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        return Map.of("doctors",
                filterDoctorByTime(doctorRepository.findAll(), amOrPm));
    }

    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        return Map.of("doctors",
                filterDoctorByTime(doctorRepository.findByNameLike(name), amOrPm));
    }

    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {

        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);

        return Map.of("doctors", filterDoctorByTime(doctors, amOrPm));
    }

    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {

        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);

        return Map.of("doctors", filterDoctorByTime(doctors, amOrPm));
    }

    // =========================
    // AVAILABILITY
    // =========================

    public boolean isAvailableInPeriod(List<Appointment> appointments, String amOrPm) {

        return appointments.stream().noneMatch(a -> {
            int hour = a.getAppointmentTime().getHour();

            if (amOrPm.equalsIgnoreCase("AM")) {
                return hour < 12;
            } else {
                return hour >= 12;
            }
        });
    }

    // =========================
    // PRIVATE FILTER HELPERS
    // =========================

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {

        List<Doctor> result = new ArrayList<>();

        for (Doctor doctor : doctors) {

            List<Appointment> appointments =
                    appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                            doctor.getId(),
                            LocalDateTime.now().toLocalDate().atStartOfDay(),
                            LocalDateTime.now().toLocalDate().atTime(23, 59)
                    );

            if (isAvailableInPeriod(appointments, amOrPm)) {
                result.add(doctor);
            }
        }

        return result;
    }
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {

        LocalDateTime startOfDay = date.atTime(9, 0);
        LocalDateTime endOfDay = date.atTime(17, 0);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        startOfDay,
                        endOfDay
                );

        List<String> allSlots = generateWorkingHours();
        List<String> occupiedSlots = new ArrayList<>();

        for (Appointment appt : appointments) {
            String time = appt.getAppointmentTime()
                    .toLocalTime()
                    .toString()
                    .substring(0, 5);

            occupiedSlots.add(time);
        }

        allSlots.removeAll(occupiedSlots);

        return allSlots;
    }

    // ==========================================
    // WORKING HOURS GENERATOR
    // ==========================================

    private List<String> generateWorkingHours() {

        List<String> slots = new ArrayList<>();

        for (int hour = 9; hour < 17; hour++) {
            slots.add(String.format("%02d:00", hour));
        }

        return slots;
    }
}