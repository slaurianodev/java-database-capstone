package com.project.backend.repository;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find patient by email
    Patient findByEmail(String email);

    // Find patient by email or phone
    Patient findByEmailOrPhone(String email, String phone);
}

