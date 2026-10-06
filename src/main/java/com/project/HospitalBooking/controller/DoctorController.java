package com.project.HospitalBooking.controller;

import com.project.HospitalBooking.Service.Impl.DoctorAvailabilityServiceImpl;
import com.project.HospitalBooking.dto.DoctorAvailabilityDto;
import com.project.HospitalBooking.entity.DoctorAvailability;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.HospitalBooking.Service.DoctorService;
import com.project.HospitalBooking.dto.DoctorDto;
import com.project.HospitalBooking.entity.Doctor;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController{

    @Autowired
    private DoctorService doctorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Doctor createDoctor(@Valid @RequestBody DoctorDto doctorDto){
        return doctorService.createDoctor(doctorDto);
    }
    @GetMapping
    public List<Doctor> getAllDoctors(){
        return doctorService.getAllDoctors();
    }

    @PatchMapping("/{doctorId}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reactivateDoctor(
            @PathVariable Integer doctorId) {

        doctorService.reactivateDoctor(doctorId);

        return ResponseEntity.ok("Doctor reactivated successfully");
    }
    @PatchMapping("/{doctorId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateDoctor(
            @PathVariable Integer doctorId) {

        doctorService.deactivateDoctor(doctorId);

        return ResponseEntity.ok("Doctor deactivated successfully");
    }
}