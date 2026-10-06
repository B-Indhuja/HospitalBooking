package com.project.HospitalBooking.controller;


import com.project.HospitalBooking.Service.Impl.DoctorAvailabilityServiceImpl;
import com.project.HospitalBooking.dto.DoctorAvailabilityDto;
import com.project.HospitalBooking.entity.DoctorAvailability;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor-availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityServiceImpl doctorAvailabilityService;
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public DoctorAvailability addAvailability(@Valid @RequestBody DoctorAvailabilityDto doctorAvailabilityDto) {

        return doctorAvailabilityService.addAvailability(doctorAvailabilityDto);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<DoctorAvailabilityDto> getAllDoctorAvailability() {
        return doctorAvailabilityService.getAllDoctorAvailability();
    }
}
