package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.Shift;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class AppointmentDto {

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotNull(message = "Appointment Date  is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Shift ID is required")
    private Shift shift;

}
