// components/patientRows.js

export function createPatientRow(patient, appointment) {
  const row = document.createElement("tr");

  // Extract data safely
  const name = patient.name || "N/A";
  const email = patient.email || "N/A";
  const phone = patient.phone || "N/A";
  const time = appointment.time || "N/A";
  const status = appointment.status || "Scheduled";

  // Create cells
  row.innerHTML = `
    <td>${name}</td>
    <td>${email}</td>
    <td>${phone}</td>
    <td>${time}</td>9
    <td>${status}</td>
  `;

  return row;
}