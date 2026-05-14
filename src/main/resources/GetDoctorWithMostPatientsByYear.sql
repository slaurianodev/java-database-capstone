DELIMITER //

CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN p_year INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        d.specialty AS doctor_specialty,
        d.email AS doctor_email,
        d.phone AS doctor_phone,
        COUNT(DISTINCT a.patient_id) AS total_unique_patients,
        COUNT(a.id) AS total_appointments,
        SUM(CASE WHEN a.status = 1 THEN 1 ELSE 0 END) AS completed_appointments,
        SUM(CASE WHEN a.status = 0 THEN 1 ELSE 0 END) AS scheduled_appointments,
        MIN(a.appointment_time) AS first_appointment,
        MAX(a.appointment_time) AS last_appointment,
        YEAR(a.appointment_time) AS appointment_year
    FROM
        doctor d
    LEFT JOIN
        appointment a ON d.id = a.doctor_id
    WHERE
        YEAR(a.appointment_time) = p_year
    GROUP BY
        d.id, d.name, d.specialty, d.email, d.phone
    ORDER BY
        total_unique_patients DESC,
        total_appointments DESC
    LIMIT 1;
END //

DELIMITER ;