package com.project.HospitalBooking.dto;

import com.project.HospitalBooking.enums.Shift;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AvailableDoctorDto {

        private Integer doctorId;
        private String doctorName;
        private String specialization;

        private LocalDate availabilityDate;
        private Shift shift;

        private Integer remainingSlots;

}
