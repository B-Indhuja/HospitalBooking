package com.project.HospitalBooking.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Patient createPatient(@Valid @RequestBody PatientDto patientDto){
        return patientService.createPatient(patientDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @PatchMapping("/{patientId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivatePatient(
            @PathVariable Integer patientId) {

        patientService.deactivatePatient(patientId);

        return ResponseEntity.ok("Patient deactivated successfully");
    }

    @PatchMapping("/{patientId}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reactivatePatient(
            @PathVariable Integer patientId) {

        patientService.reactivatePatient(patientId);

        return ResponseEntity.ok("Patient reactivated successfully");
    }
    
}
