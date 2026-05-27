// adminDashboard.js

// Import Required Modules
import { openModal } from '../components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';


// ==============================
// Event Binding
// ==============================
window.addEventListener('DOMContentLoaded', () => {

  // Add Doctor Button
  const addDocBtn = document.getElementById('addDocBtn');
  if (addDocBtn) {
    addDocBtn.addEventListener('click', () => {
      openModal('addDoctor');
    });
  }

  // Load doctors on page load
  loadDoctorCards();

  // Search & Filter Listeners
  document.getElementById("searchBar")?.addEventListener("input", filterDoctorsOnChange);
  document.getElementById("filterTime")?.addEventListener("change", filterDoctorsOnChange);
  document.getElementById("filterSpecialty")?.addEventListener("change", filterDoctorsOnChange);
});


// ==============================
// Load Doctor Cards
// ==============================
async function loadDoctorCards() {
  const contentDiv = document.getElementById("content");
  if (!contentDiv) return;

  contentDiv.innerHTML = "";

  const doctors = await getDoctors();

  renderDoctorCards(doctors);
}


// ==============================
// Render Doctor Cards
// ==============================
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  if (!contentDiv) return;

  contentDiv.innerHTML = "";

  if (!doctors || doctors.length === 0) {
    contentDiv.innerHTML = "<p>No doctors found</p>";
    return;
  }

  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}


// ==============================
// Filter Doctors
// ==============================
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar")?.value || "";
  const time = document.getElementById("filterTime")?.value || "";
  const specialty = document.getElementById("filterSpecialty")?.value || "";

  const doctors = await filterDoctors(name, time, specialty);

  renderDoctorCards(doctors);
}


// ==============================
// Add Doctor Handler
// ==============================
window.adminAddDoctor = async function () {
  try {
    // Get token from localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      alert("Unauthorized! Please login again.");
      return;
    }

    // Collect form values
    const name = document.getElementById('docName')?.value;
    const specialty = document.getElementById('docSpecialty')?.value;
    const email = document.getElementById('docEmail')?.value;
    const password = document.getElementById('docPassword')?.value;
    const mobile = document.getElementById('docMobile')?.value;

    // Collect availability (checkboxes)
    const availabilityCheckboxes = document.querySelectorAll('input[name="availability"]:checked');
    const availability = Array.from(availabilityCheckboxes).map(cb => cb.value);

    // Build doctor object
    const doctor = {
      name,
      specialty,
      email,
      password,
      mobile,
      availability
    };

    // Send request
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert(result.message || "Doctor added successfully");

      // Close modal
      const modal = document.getElementById('addDoctorModal');
      if (modal) modal.style.display = 'none';

      // Reload doctor list
      loadDoctorCards();

    } else {
      alert(result.message || "Failed to add doctor");
    }

  } catch (error) {
    console.error('Error adding doctor:', error);
    alert("An error occurred while adding doctor");
  }
};