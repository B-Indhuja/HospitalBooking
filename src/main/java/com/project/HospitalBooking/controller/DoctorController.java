package com.project.HospitalBooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.HospitalBooking.Service.DoctorService;
import com.project.HospitalBooking.dto.DoctorDto;
import com.project.HospitalBooking.entity.Doctor;

@RestController
@RequestMapping("/doctors")
public class DoctorController{

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public Doctor createDoctor(@RequestBody DoctorDto doctorDto){
        return doctorService.createDoctor(doctorDto);
    }
}