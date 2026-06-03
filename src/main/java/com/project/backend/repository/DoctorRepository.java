package com.project.backend.repository;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Find doctor by email
    Doctor findByEmail(String email);

    // Find doctors by partial name match
    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> findByNameLike(String name);

    // Filter by partial name and exact specialty (case-insensitive)
    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    // Find doctors by specialty (case-insensitive)
    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
