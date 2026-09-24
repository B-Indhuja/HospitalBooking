package com.project.HospitalBooking.controller;

import com.project.HospitalBooking.Service.Impl.DoctorAvailabilityServiceImpl;
import com.project.HospitalBooking.dto.DoctorAvailabilityDto;
import com.project.HospitalBooking.entity.DoctorAvailability;
import org.springframework.beans.factory.annotation.Autowired;
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



    @PostMapping
    public Doctor createDoctor(@RequestBody DoctorDto doctorDto){
        return doctorService.createDoctor(doctorDto);
    }
    @GetMapping
    public List<Doctor> getAllDoctors(){
        return doctorService.getAllDoctors();
    }

}