// =========================
// Render Footer Function
// =========================
export function renderFooter() {
  const footer = document.getElementById("footer");

  // Safety check
  if (!footer) return;

  // =========================
  // Inject Footer HTML
  // =========================
  footer.innerHTML = `
    <footer class="footer">

      <!-- Branding -->
      <div class="footer-brand">
        <h3>HealthCare App</h3>
        <p>Your trusted platform for booking medical appointments.</p>
      </div>

      <!-- Links Section -->
      <div class="footer-links">

        <!-- Company -->
        <div class="footer-column">
          <h4>Company</h4>
          <a href="#">About</a>
          <a href="#">Careers</a>
          <a href="#">Press</a>
        </div>

        <!-- Support -->
        <div class="footer-column">
          <h4>Support</h4>
          <a href="#">Account</a>
          <a href="#">Help Center</a>
          <a href="#">Contact</a>
        </div>

        <!-- Legals -->
        <div class="footer-column">
          <h4>Legals</h4>
          <a href="#">Terms</a>
          <a href="#">Privacy Policy</a>
          <a href="#">Licensing</a>
        </div>

      </div>

      <!-- Bottom -->
      <div class="footer-bottom">
        <p>&copy; 2026 HealthCare App. All rights reserved.</p>
      </div>

    </footer>
  `;
}

// =========================
// Auto-run on load
// =========================
renderFooter();