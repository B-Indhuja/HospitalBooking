package com.project.HospitalBooking.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name="doctor_id",nullable = false)
    private Doctor doctor;
    
    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable=false)
    private LocalTime appointmentTime;
    

    public Integer getAppointmentId(){
        return appointmentId;
    }

    public void setPatient(Patient patient){
        this.patient=patient;
    }

    public Patient getPatient(){
        return patient;
    }

    public void setDoctor(Doctor doctor){
        this.doctor=doctor;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public void setAppointmentDate(LocalDate date){
        this.appointmentDate=date;
    }

    public LocalDate getAppointmentDate()
    {
        return appointmentDate;
    }

    public void setAppointmentTime(LocalTime time){
        this.appointmentTime=time;
    }

    public LocalTime getAppointmentTime(){
        return appointmentTime;
    }
}
