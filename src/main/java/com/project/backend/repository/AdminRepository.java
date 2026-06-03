package com.project.backend.repository;

/**
 * Created by Sergio.
 */
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.backend.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Custom query method
    Admin findByUsername(String username);
}

