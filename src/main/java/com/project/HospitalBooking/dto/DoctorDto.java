package com.project.HospitalBooking.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDto{

    @NotBlank(message="Doctor name is required")
    @Pattern(regexp = "[A-Za-z ]+",message="Doctor name should only contain letters and spaces")
    private String doctorName;

    @NotBlank(message="Doctor gender is required")
    @Pattern(regexp = "[A-Za-z]+",message="Doctor gender should only contain letters")
    private String doctorGender;

    @NotBlank(message = "Doctor specialization is required")
    @Size(max=30,message="Specialization should not exceed 30 characters")
    @Pattern(regexp = "[A-Za-z ]+",message="Doctor specialization should only contain letters and spaces")
    private String specialization;

    @NotBlank(message = "Doctor phone number is required")
    @Pattern(regexp = "\\d{10}",message = "Phone number should be exactly 10 digits")
    private String doctorPhoneNumber;

}