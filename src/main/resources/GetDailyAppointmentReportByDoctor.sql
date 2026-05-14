DELIMITER //

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN p_appointment_date DATE
)
BEGIN
    SELECT
        a.id AS appointment_id,
        d.id AS doctor_id,
        d.name AS doctor_name,
        d.specialty AS doctor_specialty,
        d.email AS doctor_email,
        d.phone AS doctor_phone,
        p.id AS patient_id,
        p.name AS patient_name,
        p.email AS patient_email,
        p.phone AS patient_phone,
        a.appointment_time AS appointment_time,
        TIME(a.appointment_time) AS appointment_time_only,
        DATE_ADD(a.appointment_time, INTERVAL 1 HOUR) AS end_time,
        CASE
            WHEN a.status = 0 THEN 'Scheduled'
            WHEN a.status = 1 THEN 'Completed'
            ELSE 'Unknown'
        END AS status_text,
        a.status AS status_code,
        a.created_at AS appointment_created_at,
        a.updated_at AS appointment_updated_at
    FROM
        appointment a
    INNER JOIN
        doctor d ON a.doctor_id = d.id
    INNER JOIN
        patient p ON a.patient_id = p.id
    WHERE
        DATE(a.appointment_time) = p_appointment_date
    ORDER BY
        d.id ASC,
        a.appointment_time ASC;
END //

DELIMITER ;