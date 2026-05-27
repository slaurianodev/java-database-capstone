// patientDashboard.js

// ==============================
// Import Required Modules
// ==============================
import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientLogin, patientSignup } from "./services/patientServices.js";


// ==============================
// Load Doctor Cards on Page Load
// ==============================
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();

  // Modal triggers
  const signupBtn = document.getElementById("patientSignup");
  if (signupBtn) {
    signupBtn.addEventListener("click", () => openModal("patientSignup"));
  }

  const loginBtn = document.getElementById("patientLogin");
  if (loginBtn) {
    loginBtn.addEventListener("click", () => openModal("patientLogin"));
  }

  // Search & Filter listeners
  document.getElementById("searchBar")?.addEventListener("input", filterDoctorsOnChange);
  document.getElementById("filterTime")?.addEventListener("change", filterDoctorsOnChange);
  document.getElementById("filterSpecialty")?.addEventListener("change", filterDoctorsOnChange);
});


// ==============================
// Load Doctors
// ==============================
async function loadDoctorCards() {
  const contentDiv = document.getElementById("content");
  if (!contentDiv) return;

  contentDiv.innerHTML = "";

  try {
    const doctors = await getDoctors();

    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = "<p>No doctors available.</p>";
      return;
    }

    doctors.forEach(doctor => {
      const card = createDoctorCard(doctor);
      contentDiv.appendChild(card);
    });

  } catch (error) {
    console.error("Error loading doctors:", error);
    contentDiv.innerHTML = "<p>Failed to load doctors.</p>";
  }
}


// ==============================
// Filter Doctors
// ==============================
function filterDoctorsOnChange() {
  const contentDiv = document.getElementById("content");

  const name = document.getElementById("searchBar")?.value || "";
  const time = document.getElementById("filterTime")?.value || "";
  const specialty = document.getElementById("filterSpecialty")?.value || "";

  filterDoctors(name, time, specialty)
    .then(doctors => {
      contentDiv.innerHTML = "";

      if (!doctors || doctors.length === 0) {
        contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
        return;
      }

      doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
      });
    })
    .catch(error => {
      console.error("Error filtering doctors:", error);
      contentDiv.innerHTML = "<p>Error fetching filtered doctors.</p>";
    });
}


// ==============================
// Patient Signup
// ==============================
window.signupPatient = async function () {
  try {
    const name = document.getElementById("signupName")?.value;
    const email = document.getElementById("signupEmail")?.value;
    const password = document.getElementById("signupPassword")?.value;
    const phone = document.getElementById("signupPhone")?.value;
    const address = document.getElementById("signupAddress")?.value;

    const data = { name, email, password, phone, address };

    const result = await patientSignup(data);

    if (result.success) {
      alert(result.message || "Signup successful");

      const modal = document.getElementById("patientSignupModal");
      if (modal) modal.style.display = "none";

      location.reload();
    } else {
      alert(result.message || "Signup failed");
    }

  } catch (error) {
    console.error("Signup error:", error);
    alert("An error occurred during signup");
  }
};


// ==============================
// Patient Login
// ==============================
window.loginPatient = async function () {
  try {
    const email = document.getElementById("loginEmail")?.value;
    const password = document.getElementById("loginPassword")?.value;

    const data = { email, password };

    const response = await patientLogin(data);

    if (!response) {
      alert("Login failed");
      return;
    }

    const result = await response.json();

    if (response.ok) {
      localStorage.setItem("token", result.token);
      window.location.href = "loggedPatientDashboard.html";
    } else {
      alert(result.message || "Invalid credentials");
    }

  } catch (error) {
    console.error("Login error:", error);
    alert("An error occurred during login");
  }
};