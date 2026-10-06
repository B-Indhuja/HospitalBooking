package com.project.HospitalBooking.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PatientRegistrationDto {

    @NotBlank(message = "Patient name is required")
    @Size(max = 30, message = "Patient name cannot exceed 30 characters")
    @Pattern(
            regexp = "[A-Za-z ]+",
            message = "Patient name should only contain letters and spaces"
    )
    private String patientName;

    @NotBlank(message = "Patient gender is required")
    @Pattern(
            regexp = "[A-Za-z]+",
            message = "Patient gender should only contain letters"
    )
    private String patientGender;

    @NotNull(message = "Patient age is required")
    @Min(value = 1, message = "Patient age must be at least 1")
    @Max(value = 120, message = "Patient age cannot exceed 120")
    private Integer patientAge;

    @NotBlank(message = "Patient phone number is required")
    @Pattern(
            regexp = "\\d{10}",
            message = "Patient phone number must be exactly 10 digits"
    )
    private String patientPhoneNumber;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;


}