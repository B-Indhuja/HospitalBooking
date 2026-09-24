package com.project.HospitalBooking.controller;


import com.project.HospitalBooking.Service.Impl.DoctorAvailabilityServiceImpl;
import com.project.HospitalBooking.dto.DoctorAvailabilityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor-availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityServiceImpl doctorAvailabilityService;
    @PostMapping
    public com.project.HospitalBooking.entity.DoctorAvailability addAvailability(
            @RequestBody DoctorAvailabilityDto doctorAvailabilityDto) {

        return doctorAvailabilityService.addAvailability(doctorAvailabilityDto);
    }
    @GetMapping
    public List<DoctorAvailabilityDto> getAllDoctorAvailability() {
        return doctorAvailabilityService.getAllDoctorAvailability();
    }
}
