package com.project.backend.models;

/**
 * Created by Sergio.
 */
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;

    @NotBlank(message = "Patient name cannot be blank")
    @Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters")
    private String patientName;

    @NotNull(message = "Appointment ID cannot be null")
    private Long appointmentId;

    @NotBlank(message = "Medication cannot be blank")
    @Size(min = 3, max = 100, message = "Medication must be between 3 and 100 characters")
    private String medication;

    @NotBlank(message = "Dosage cannot be blank")
    @Size(min = 3, max = 20, message = "Dosage must be between 3 and 20 characters")
    private String dosage;

    @Size(max = 200, message = "Doctor notes must not exceed 200 characters")
    private String doctorNotes;

    // Constructors
    public Prescription() {
    }

    public Prescription(String patientName, Long appointmentId, String medication, String dosage) {
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
    }

    public Prescription(String patientName, Long appointmentId, String medication, String dosage, String doctorNotes) {
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
        this.doctorNotes = doctorNotes;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    // Helper Methods
    /**
     * Returns a formatted prescription summary
     * @return Formatted prescription information
     */
    public String getPrescriptionSummary() {
        return "Prescription for " + patientName +
                " | Medication: " + medication +
                " | Dosage: " + dosage +
                (doctorNotes != null ? " | Notes: " + doctorNotes : "");
    }

    /**
     * Checks if doctor notes are provided
     * @return true if doctor notes exist, false otherwise
     */
    public boolean hasDoctorNotes() {
        return doctorNotes != null && !doctorNotes.isEmpty();
    }

    /**
     * Returns a detailed prescription information
     * @return Detailed prescription string
     */
    public String getDetailedInfo() {
        return "Prescription ID: " + id +
                " | Patient: " + patientName +
                " | Appointment ID: " + appointmentId +
                " | Medication: " + medication +
                " | Dosage: " + dosage +
                (hasDoctorNotes() ? " | Doctor Notes: " + doctorNotes : "");
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id='" + id + '\'' +
                ", patientName='" + patientName + '\'' +
                ", appointmentId=" + appointmentId +
                ", medication='" + medication + '\'' +
                ", dosage='" + dosage + '\'' +
                ", doctorNotes='" + doctorNotes + '\'' +
                '}';
    }
}
