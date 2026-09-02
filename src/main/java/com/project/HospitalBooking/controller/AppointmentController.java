package com.project.HospitalBooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.HospitalBooking.Service.AppointmentService;
import com.project.HospitalBooking.dto.AppointmentDto;
import com.project.HospitalBooking.entity.Appointment;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public Appointment createAppointment(@RequestBody AppointmentDto appointment){
        return appointmentService.createAppointment(appointment);
    }
    
    @DeleteMapping("/{appointmentId}")
    public void cancelAppointment(@PathVariable Integer appointmentId){
        appointmentService.cancelAppointment(appointmentId);
    }
}
