package com.project.HospitalBooking.dto;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationDto {

        @NotBlank(message = "Doctor name is required")
        @Size(max = 30, message = "Doctor name cannot exceed 30 characters")
        @Pattern(
                regexp = "[A-Za-z ]+",
                message = "Doctor name should only contain letters and spaces"
        )
        private String doctorName;

        @NotBlank(message = "Doctor gender is required")
        @Pattern(
                regexp = "[A-Za-z]+",
                message = "Doctor gender should only contain letters"
        )
        private String doctorGender;

        @NotBlank(message = "Doctor specialization is required")
        @Size(max = 50, message = "Doctor specialization cannot exceed 50 characters")
        private String specialization;

        @NotBlank(message = "Doctor phone number is required")
        @Pattern(
                regexp = "\\d{10}",
                message = "Phone number should be exactly 10 digits"
        )
        private String doctorPhoneNumber;

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
