package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.dto.PatientDto;
import com.project.HospitalBooking.entity.Patient;
import com.project.HospitalBooking.entity.User;

import java.util.List;

public interface PatientService {
    Patient createPatient(PatientDto patientDto);
    List<Patient> getAllPatients();
    Patient savePatient(Patient patient);
}
