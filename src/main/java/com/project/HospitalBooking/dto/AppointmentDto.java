package com.project.HospitalBooking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {
    private Integer patientId;
    private Integer doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

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
