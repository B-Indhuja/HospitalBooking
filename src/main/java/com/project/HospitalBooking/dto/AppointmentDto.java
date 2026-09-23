package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.Shift;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {
    private Integer patientId;
    private Integer doctorId;
    private LocalDate appointmentDate;
    private Shift shift;

    public void setPatientId(Integer id){
        this.patientId=id;
    }

    public Integer getPatientId(){
        return patientId;
    }

    public void setDoctorId(Integer id){
        this.doctorId=id;
    }

    public Integer getDoctorId(){
        return doctorId;
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
}
