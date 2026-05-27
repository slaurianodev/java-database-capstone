// modals.js

// Helper to close all modals
function closeAllModals() {
  const modals = document.querySelectorAll('.modal');
  modals.forEach(modal => {
    modal.style.display = 'none';
  });
}

// Open Modal Function (used in index.js)
export function openModal(type) {
  closeAllModals();

  let modalId = '';

  if (type === 'adminLogin') {
    modalId = 'adminModal';
  } else if (type === 'doctorLogin') {
    modalId = 'doctorModal';
  }

  const modal = document.getElementById(modalId);

  if (modal) {
    modal.style.display = 'block';
  }
}

// Optional: Close modal when clicking close buttons
window.addEventListener('DOMContentLoaded', () => {
  const closeButtons = document.querySelectorAll('.close-modal');

  closeButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      closeAllModals();
    });
  });

  // Optional: Close when clicking outside modal content
  window.addEventListener('click', (event) => {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
      if (event.target === modal) {
        modal.style.display = 'none';
      }
    });
  });
});