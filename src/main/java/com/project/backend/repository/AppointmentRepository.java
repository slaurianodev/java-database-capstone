package com.project.backend.repository;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Retrieve appointments for a doctor within a given time range
    @Query("SELECT a FROM Appointment a " +
            "LEFT JOIN FETCH a.doctor d " +
            "LEFT JOIN FETCH a.availability av " +
            "WHERE d.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId,
                                                              LocalDateTime start,
                                                              LocalDateTime end);

    // Filter appointments by doctor ID, patient name, and time range
    @Query("SELECT a FROM Appointment a " +
            "LEFT JOIN FETCH a.doctor d " +
            "LEFT JOIN FETCH a.patient p " +
            "WHERE d.id = :doctorId " +
            "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%')) " +
            "AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            Long doctorId,
            String patientName,
            LocalDateTime start,
            LocalDateTime end
    );

    // Delete all appointments by doctor ID
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    // Find all appointments for a specific patient
    List<Appointment> findByPatientId(Long patientId);

    // Retrieve appointments for a patient by status, ordered by time
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    // Search appointments by partial doctor name and patient ID
    @Query("SELECT a FROM Appointment a " +
            "JOIN a.doctor d " +
            "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) " +
            "AND a.patient.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

    // Filter appointments by doctor name, patient ID, and status
    @Query("SELECT a FROM Appointment a " +
            "JOIN a.doctor d " +
            "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) " +
            "AND a.patient.id = :patientId " +
            "AND a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName,
                                                              Long patientId,
                                                              int status);
}
