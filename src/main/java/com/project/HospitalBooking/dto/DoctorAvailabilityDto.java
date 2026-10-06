package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.Shift;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class DoctorAvailabilityDto {
    private Integer doctorId;
    private LocalDate availabilityDate;
    private Shift shift;
    private Integer maxAppointments;


}
