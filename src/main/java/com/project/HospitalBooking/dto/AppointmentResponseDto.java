package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.enums.Shift;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
public class AppointmentResponseDto {

    private  Integer appointmentId;
    private Integer patientId;
    private Integer doctorId;
    private String doctorName;
    private LocalDate appointmentDate;
    private Shift shift;
    private AppointmentStatus appointmentStatus;

}
