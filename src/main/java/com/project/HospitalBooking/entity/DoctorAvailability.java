package com.project.HospitalBooking.entity;
import com.project.HospitalBooking.enums.Shift;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="doctor_availability")
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer availabilityId;

    @ManyToOne
    @JoinColumn(name="doctor_id",nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate availabilityDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Shift shift;

    @Column(nullable = false)
    private Integer maxAppointments;

    public void setAvailabilityId(Integer id){
        this.availabilityId=id;
    }

    public Integer getAvailabilityId(){
        return availabilityId;
    }

    public void setDoctor(Doctor doctor){
        this.doctor=doctor;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public void setAvailabilityDate(LocalDate date){
        this.availabilityDate=date;
    }

    public LocalDate getAvailabilityDate(){
        return availabilityDate;
    }

    public void setShift(Shift shift){
        this.shift=shift;
    }

    public Shift getShift(){
        return shift;
    }

    public void setMaxAppointments(Integer maxAppointments){
        this.maxAppointments=maxAppointments;
    }

    public Integer getMaxAppointments(){
        return maxAppointments;
    }
}
