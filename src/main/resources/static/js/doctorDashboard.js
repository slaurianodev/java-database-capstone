// doctorDashboard.js

// Import Required Modules
import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";


// ==============================
// Global Variables
// ==============================
const tableBody = document.getElementById("patientTableBody");
let selectedDate = new Date().toISOString().split("T")[0]; // today's date
const token = localStorage.getItem("token");
let patientName = null;


// ==============================
// Setup Event Listeners
// ==============================
window.addEventListener("DOMContentLoaded", () => {
  const searchBar = document.getElementById("searchBar");
  const todayButton = document.getElementById("todayButton");
  const datePicker = document.getElementById("datePicker");

  // Search bar functionality
  if (searchBar) {
    searchBar.addEventListener("input", (e) => {
      const value = e.target.value.trim();
      patientName = value === "" ? null : value;
      loadAppointments();
    });
  }

  // Today button
  if (todayButton) {
    todayButton.addEventListener("click", () => {
      selectedDate = new Date().toISOString().split("T")[0];
      if (datePicker) datePicker.value = selectedDate;
      loadAppointments();
    });
  }

  // Date picker filter
  if (datePicker) {
    datePicker.addEventListener("change", (e) => {
      selectedDate = e.target.value;
      loadAppointments();
    });
  }

  // Initial load
  loadAppointments();
});


// ==============================
// Load Appointments
// ==============================
export async function loadAppointments() {
  try {
    const tableBody = document.getElementById("patientTableBody");
    if (!tableBody) return;

    tableBody.innerHTML = "";

    const appointments = await getAllAppointments(selectedDate, patientName, token);

    // No appointments found
    if (!appointments || appointments.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="5" style="text-align:center;">
            No Appointments found for selected date
          </td>
        </tr>
      `;
      return;
    }

    // Render appointments
    appointments.forEach(appointment => {
      const patient = appointment.patient || appointment;
      const row = createPatientRow(patient, appointment);
      tableBody.appendChild(row);
    });

  } catch (error) {
    console.error("Error loading appointments:", error);

    const tableBody = document.getElementById("patientTableBody");
    if (tableBody) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="5" style="text-align:center; color:red;">
            Error loading appointments. Please try again later.
          </td>
        </tr>
      `;
    }
  }
}