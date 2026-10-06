package com.project.HospitalBooking.controller;


import com.project.HospitalBooking.Service.AuthService;
import com.project.HospitalBooking.dto.DoctorRegistrationDto;
import com.project.HospitalBooking.dto.LoginRequestDto;
import com.project.HospitalBooking.dto.PatientRegistrationDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/patient")
    public ResponseEntity<String> registerPatient(
            @Valid @RequestBody PatientRegistrationDto request) {

        authService.registerPatient(request);

        return ResponseEntity.ok("Patient registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequestDto loginRequest) {

        String token = authService.login(loginRequest);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register/doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerDoctor(
            @Valid @RequestBody DoctorRegistrationDto request) {

        authService.registerDoctor(request);

        return ResponseEntity.ok("Doctor registered successfully");
    }
}
