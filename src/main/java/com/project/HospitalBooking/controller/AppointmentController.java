package com.project.HospitalBooking.controller;

import com.project.HospitalBooking.dto.AppointmentResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.HospitalBooking.Service.AppointmentService;
import com.project.HospitalBooking.dto.AppointmentDto;
import com.project.HospitalBooking.entity.Appointment;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public AppointmentResponseDto createAppointment(@Valid @RequestBody AppointmentDto appointment){
        return appointmentService.createAppointment(appointment);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
    
    @PatchMapping("/cancel/{appointmentId}")
    @PreAuthorize("hasRole('PATIENT')")
    public void cancelAppointment(@PathVariable Integer appointmentId){
        appointmentService.cancelAppointment(appointmentId);
    }

    @PatchMapping("/complete/{appointmentId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public  void completeAppointment(@PathVariable Integer appointmentId){
        appointmentService.completeAppointment(appointmentId);
    }

}
