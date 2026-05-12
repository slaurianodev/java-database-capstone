# MySQL Schema

## Patients
CREATE TABLE Patients (
Id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
birthdate DATE NOT NULL,
documentNumber VARCHAR(100) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(200) NOT NULL
);

## Doctors
CREATE TABLE Doctors (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
specialty VARCHAR(100) NOT NULL,
medicalID VARCHAR(100) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(100) NOT NULL
);

## Appointments
CREATE TABLE Appointments (
id INT AUTO_INCREMENT PRIMARY KEY,
doctorID INT NOT NULL,
patientID INT NOT NULL,
appointmentDate DATETIME NOT NULL,
notes VARCHAR(500),
FOREIGN KEY (doctorID) REFERENCES Doctors(id),
FOREIGN KEY (patientID) REFERENCES Patients(Id)
);

## Prescriptions
CREATE TABLE Prescriptions (
Id INT AUTO_INCREMENT PRIMARY KEY,
appointmentID INT NOT NULL,
note VARCHAR(500),
FOREIGN KEY (appointmentID) REFERENCES Appointments(id)
);

## Admin
CREATE TABLE Admin (
id INT AUTO_INCREMENT PRIMARY KEY,
email VARCHAR(100) NOT NULL UNIQUE,
name VARCHAR(100) NOT NULL,
password VARCHAR(200) NOT NULL
);

# MongoDB Schema
## Patients
const patientSchema = {  
_id: ObjectId,  
name: String,  
birthdate: Date,  
documentNumber: String,  
email: String,  
password: String  
};

## Doctors
const doctor = {  
_id: ObjectId,  
name: String,  
specialty: String,  
medicalID: String,  
email: String,  
password: String  
};

## Appointments
const appointments = {  
_id: ObjectId,  
doctorID: ObjectId,  
patientID: ObjectId,  
appointmentDate: Date,  
notes: String  
};  

## Prescription
const prescriptions = {  
_id: ObjectId,  
appointmentID: ObjectId,  
note: String  
};  

## Admin
const admin = {  
_id: ObjectId,  
email: String,  
name: String,  
password: String  
};  