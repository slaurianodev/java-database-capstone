// =========================
// Create Doctor Card
// =========================
export function createDoctorCard(doctor) {

  // Main Card Container
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // Fetch User Role
  const role = localStorage.getItem("userRole");

  // =========================
  // Doctor Info Section
  // =========================
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Specialty: ${doctor.specialization}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  const availability = document.createElement("p");
  availability.textContent = `Available: ${
    Array.isArray(doctor.availability)
      ? doctor.availability.join(", ")
      : doctor.availability
  }`;

  // Append info elements
  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);

  // =========================
  // Actions Section
  // =========================
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  // =========================
  // Role-Based Buttons
  // =========================

  // Admin: Delete Doctor
  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Delete";

    removeBtn.addEventListener("click", async () => {
      const confirmDelete = confirm("Are you sure you want to delete this doctor?");
      if (!confirmDelete) return;

      try {
        const token = localStorage.getItem("token");

        const response = await fetch(`/api/doctors/${doctor.id}`, {
          method: "DELETE",
          headers: {
            "Authorization": `Bearer ${token}`
          }
        });

        if (response.ok) {
          card.remove(); // remove from DOM
        } else {
          alert("Failed to delete doctor.");
        }

      } catch (error) {
        console.error("Error deleting doctor:", error);
        alert("An error occurred while deleting.");
      }
    });

    actionsDiv.appendChild(removeBtn);
  }

  // Patient (not logged in)
  else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";

    bookNow.addEventListener("click", () => {
      alert("Patient needs to login first.");
    });

    actionsDiv.appendChild(bookNow);
  }

  // Logged-in Patient
  else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";

    bookNow.addEventListener("click", async (e) => {
      try {
        const token = localStorage.getItem("token");

        // assumes these functions exist in another module
        const patientData = await getPatientData(token);
        showBookingOverlay(e, doctor, patientData);

      } catch (error) {
        console.error("Booking error:", error);
      }
    });

    actionsDiv.appendChild(bookNow);
  }

  // =========================
  // Final Assembly
  // =========================
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}