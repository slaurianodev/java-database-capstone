// patientServices.js

// Import the API Base URL
import { API_BASE_URL } from "../config/config.js";

// Set the Base Patient API Endpoint
const PATIENT_API = API_BASE_URL + '/patient';


// ==============================
// Patient Signup
// ==============================
export async function patientSignup(data) {
  try {
    // Send POST request with patient data
    const response = await fetch(`${PATIENT_API}/signup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    const resData = await response.json();

    if (response.ok) {
      return {
        success: true,
        message: resData.message || 'Signup successful'
      };
    } else {
      return {
        success: false,
        message: resData.message || 'Signup failed'
      };
    }

  } catch (error) {
    console.error('Error during patient signup:', error);
    return {
      success: false,
      message: 'An error occurred during signup'
    };
  }
}


// ==============================
// Patient Login
// ==============================
export async function patientLogin(data) {
  try {
    // (Optional for dev) console.log('Login data:', data);

    const response = await fetch(`${PATIENT_API}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    // Return full response for flexibility (token handling, etc.)
    return response;

  } catch (error) {
    console.error('Error during patient login:', error);
    return null;
  }
}


// ==============================
// Get Logged-in Patient Data
// ==============================
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/me`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      throw new Error('Failed to fetch patient data');
    }

    const data = await response.json();
    return data;

  } catch (error) {
    console.error('Error fetching patient data:', error);
    return null;
  }
}


// ==============================
// Get Patient Appointments
// ==============================
export async function getPatientAppointments(id, token, user) {
  try {
    // Dynamic endpoint based on user role
    const url = `${PATIENT_API}/appointments/${id}?user=${user}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      throw new Error('Failed to fetch appointments');
    }

    const data = await response.json();
    return data || [];

  } catch (error) {
    console.error('Error fetching appointments:', error);
    return null;
  }
}


// ==============================
// Filter Appointments
// ==============================
export async function filterAppointments(condition, name, token) {
  try {
    // Build query parameters dynamically
    const params = new URLSearchParams();

    if (condition) params.append('condition', condition);
    if (name) params.append('name', name);

    const url = `${PATIENT_API}/appointments/filter?${params.toString()}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      throw new Error('Failed to filter appointments');
    }

    const data = await response.json();
    return data || [];

  } catch (error) {
    console.error('Error filtering appointments:', error);
    alert('Something went wrong while filtering appointments');
    return [];
  }
}