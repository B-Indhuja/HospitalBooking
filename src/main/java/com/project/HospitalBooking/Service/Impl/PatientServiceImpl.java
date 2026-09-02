package com.project.HospitalBooking.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.HospitalBooking.Service.PatientService;
import com.project.HospitalBooking.dto.PatientDto;
import com.project.HospitalBooking.entity.Patient;
import com.project.HospitalBooking.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient createPatient(PatientDto patientDto){
        Patient patient=new Patient();
        patient.setPatientName(patientDto.getPatientName());
        patient.setPatientAge(patientDto.getPatientAge());
        patient.setPatientPhoneNumber(patientDto.getPatientPhoneNumber());
        return patientRepository.save(patient);
    }
    
}
