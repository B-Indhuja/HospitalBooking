package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.Shift;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class AppointmentDto {
    private Integer patientId;
    private Integer doctorId;
    private LocalDate appointmentDate;
    private Shift shift;

}
