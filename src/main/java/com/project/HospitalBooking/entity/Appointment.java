package com.project.HospitalBooking.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.enums.Shift;
import jakarta.persistence.*;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Shift shift;
    

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

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setShift(Shift shift){
        this.shift=shift;
    }

    public Shift getShift(){
        return shift;
    }

    public  void setAppointmentStatus(AppointmentStatus status){
        this.status=status;
    }

    public AppointmentStatus getAppointmentStatus(){
        return status;
    }
}
