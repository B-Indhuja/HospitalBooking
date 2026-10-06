package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.Shift;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class DoctorAvailabilityDto {

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotNull(message = "Availability date is required")
    private LocalDate availabilityDate;

    @NotNull(message = "Shift is required")
    private Shift shift;

    @NotNull(message = "Maximum appointments is required")
    @Min(value = 1, message = "Maximum appointments must be at least 1")
    @Max(value = 50, message = "Maximum appointments cannot exceed 50")
    private Integer maxAppointments;


}
