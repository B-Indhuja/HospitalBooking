package com.project.HospitalBooking.dto;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PatientDto {

    @NotBlank(message="Patient name is required")
    @Size(max=30,message="Patient name cannot exceed 30 characters")
    @Pattern(regexp = "[A-Za-z ]+",message="Patient name should only contain letters and spaces")
    private String patientName;


    @NotBlank(message="Patient gender is required")
    @Pattern(regexp = "[A-Za-z]+",message="Patient gender should only contain letters and ")
    private String patientGender;


    @NotNull(message="Patient age is required")
    @Min(value=1,message="Patient age must be at least 1")
    @Max(value=100,message="Patient age cannot exceed 100")
    private Integer patientAge;


    @NotBlank(message="Patient phone number is required")
    @Pattern(regexp = "\\d{10}",message = "Phone number should be exactly 10 digits")
    private String patientPhoneNumber;


}