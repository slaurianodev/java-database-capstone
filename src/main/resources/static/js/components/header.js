// =========================
// Render Header Function
// =========================
export function renderHeader() {
  const header = document.getElementById("header");
  if (!header) return;

  const currentPath = window.location.pathname;

  // Do NOT show header on homepage
  if (currentPath === "/" || currentPath.includes("index.html")) {
    header.innerHTML = "";
    return;
  }

  const token = localStorage.getItem("token");
  let role = localStorage.getItem("userRole");

  let headerHTML = "";

  // =========================
  // Role-Based Rendering
  // =========================

  // Doctor (logged in)
  if (role === "doctor" && token) {
    headerHTML = `
      <nav class="nav">
        <button id="homeBtn">Home</button>
        <button id="logoutBtn">Logout</button>
      </nav>
    `;
  }

  // Logged Patient
  else if (role === "loggedPatient" && token) {
    headerHTML = `
      <nav class="nav">
        <button id="homeBtn">Home</button>
        <button id="appointmentsBtn">Appointments</button>
        <button id="logoutBtn">Logout</button>
      </nav>
    `;
  }

  // Patient (NOT logged in)
  else if (role === "patient" || !token) {
    headerHTML = `
      <nav class="nav">
        <button id="loginBtn">Login</button>
        <button id="signupBtn">Sign Up</button>
      </nav>
    `;
  }

  // Invalid / fallback
  else {
    headerHTML = `
      <nav class="nav">
        <button id="homeBtn">Home</button>
      </nav>
    `;
  }

  // =========================
  // Inject Header
  // =========================
  header.innerHTML = headerHTML;

  // =========================
  // Attach Event Listeners
  // =========================

  const homeBtn = document.getElementById("homeBtn");
  const loginBtn = document.getElementById("loginBtn");
  const signupBtn = document.getElementById("signupBtn");
  const appointmentsBtn = document.getElementById("appointmentsBtn");
  const logoutBtn = document.getElementById("logoutBtn");

  if (homeBtn) {
    homeBtn.addEventListener("click", () => {
      window.location.href = "/dashboard";
    });
  }

  if (loginBtn) {
    loginBtn.addEventListener("click", () => {
      window.location.href = "/login";
    });
  }

  if (signupBtn) {
    signupBtn.addEventListener("click", () => {
      window.location.href = "/signup";
    });
  }

  if (appointmentsBtn) {
    appointmentsBtn.addEventListener("click", () => {
      window.location.href = "/appointments";
    });
  }

  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      // Clear session
      localStorage.removeItem("token");

      // Reset role to patient (not logged)
      localStorage.setItem("userRole", "patient");

      // Redirect to homepage
      window.location.href = "/";
    });
  }
}

// =========================
// Auto-run if needed
// =========================
document.addEventListener("DOMContentLoaded", renderHeader);