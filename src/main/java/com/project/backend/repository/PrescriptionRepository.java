package com.project.backend.repository;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    // Find prescriptions by appointment ID
    List<Prescription> findByAppointmentId(Long appointmentId);
}
