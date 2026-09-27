package com.project.HospitalBooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.HospitalBooking.Service.PatientService;
import com.project.HospitalBooking.dto.PatientDto;
import com.project.HospitalBooking.entity.Patient;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public Patient createPatient(@RequestBody PatientDto patientDto){
        return patientService.createPatient(patientDto);
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }
    
}
