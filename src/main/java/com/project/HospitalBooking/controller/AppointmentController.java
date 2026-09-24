package com.project.HospitalBooking.controller;

import com.project.HospitalBooking.dto.AppointmentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.HospitalBooking.Service.AppointmentService;
import com.project.HospitalBooking.dto.AppointmentDto;
import com.project.HospitalBooking.entity.Appointment;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public AppointmentResponseDto createAppointment(@RequestBody AppointmentDto appointment){
        return appointmentService.createAppointment(appointment);
    }
    
    @PatchMapping("/cancel/{appointmentId}")
    public void cancelAppointment(@PathVariable Integer appointmentId){
        appointmentService.cancelAppointment(appointmentId);
    }

    @PatchMapping("/complete/{appointmentId}")
    public  void completeAppointment(@PathVariable Integer appointmentId){
        appointmentService.completeAppointment(appointmentId);
    }

}
