// services/appointmentRecordService.js

const BASE_URL = "http://localhost:8080";

/**
 * Fetch all appointments filtered by date and patient name
 *
 * @param {string} date - Selected date (YYYY-MM-DD)
 * @param {string|null} patientName - Optional patient name filter
 * @param {string} token - Auth token
 * @returns {Promise<Array>}
 */
export async function getAllAppointments(date, patientName, token) {
  try {
    let url = `${BASE_URL}/appointments?date=${date}`;

    // Add patient name filter if exists
    if (patientName) {
      url += `&patientName=${encodeURIComponent(patientName)}`;
    }

    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data = await response.json();

    return data;

  } catch (error) {
    console.error("Error fetching appointments:", error);
    throw error;
  }
}