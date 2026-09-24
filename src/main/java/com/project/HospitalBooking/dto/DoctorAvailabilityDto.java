package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.Shift;

import java.time.LocalDate;

public class DoctorAvailabilityDto {
    private Integer doctorId;
    private LocalDate availabilityDate;
    private Shift shift;
    private Integer maxAppointments;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(LocalDate availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Integer getMaxAppointments() {
        return maxAppointments;
    }

    public void setMaxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
    }
}
