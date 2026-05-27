// doctorServices.js

// Import API Base URL
import { API_BASE_URL } from "../config/config.js";

// Set Doctor API Endpoint
const DOCTOR_API = API_BASE_URL + '/doctor';

// Get All Doctors
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);

    if (!response.ok) {
      throw new Error('Failed to fetch doctors');
    }

    const data = await response.json();
    return data || [];
  } catch (error) {
    console.error('Error fetching doctors:', error);
    return [];
  }
}

// Delete a Doctor
export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    const data = await response.json();

    if (response.ok) {
      return { success: true, message: data.message || 'Doctor deleted successfully' };
    } else {
      return { success: false, message: data.message || 'Failed to delete doctor' };
    }
  } catch (error) {
    console.error('Error deleting doctor:', error);
    return { success: false, message: 'An error occurred while deleting doctor' };
  }
}

// Save (Add) a New Doctor
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(doctor)
    });

    const data = await response.json();

    if (response.ok) {
      return { success: true, message: data.message || 'Doctor added successfully' };
    } else {
      return { success: false, message: data.message || 'Failed to add doctor' };
    }
  } catch (error) {
    console.error('Error saving doctor:', error);
    return { success: false, message: 'An error occurred while saving doctor' };
  }
}

// Filter Doctors
export async function filterDoctors(name, time, specialty) {
  try {
    // Build query params dynamically
    const params = new URLSearchParams();

    if (name) params.append('name', name);
    if (time) params.append('time', time);
    if (specialty) params.append('specialty', specialty);

    const url = params.toString()
      ? `${DOCTOR_API}/search?${params.toString()}`
      : DOCTOR_API;

    const response = await fetch(url);

    if (!response.ok) {
      throw new Error('Failed to filter doctors');
    }

    const data = await response.json();
    return data || [];
  } catch (error) {
    console.error('Error filtering doctors:', error);
    alert('Something went wrong while filtering doctors');
    return [];
  }
}