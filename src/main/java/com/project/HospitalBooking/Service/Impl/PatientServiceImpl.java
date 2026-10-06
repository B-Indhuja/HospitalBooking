package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.entity.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.HospitalBooking.Service.PatientService;
import com.project.HospitalBooking.dto.PatientDto;
import com.project.HospitalBooking.entity.Patient;
import com.project.HospitalBooking.repository.PatientRepository;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;
    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Override
    public Patient createPatient(PatientDto patientDto){
        Patient patient=new Patient();
        patient.setPatientName(patientDto.getPatientName());
        patient.setPatientAge(patientDto.getPatientAge());
        patient.setPatientPhoneNumber(patientDto.getPatientPhoneNumber());
        patient.setPatientGender(patientDto.getPatientGender());

        Patient savedPatient = patientRepository.save(patient);

        log.info("Patient created successfully with ID: {}",
                savedPatient.getPatientId());
        return savedPatient;
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        log.info("Fetched {} patients", patients.size());
        return patients;
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deactivatePatient(Integer patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        patient.setActive(false);
        User user = patient.getUser();
        if (user != null) {
            user.setActive(false);
        }
        patientRepository.save(patient);

        log.info("Patient {} deactivated successfully", patientId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void reactivatePatient(Integer patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        patient.setActive(true);

        User user = patient.getUser();
        if (user != null) {
            user.setActive(true);
        }

        patientRepository.save(patient);

        log.info("Patient {} reactivated successfully", patientId);
    }
}
