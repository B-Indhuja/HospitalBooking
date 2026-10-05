package com.project.HospitalBooking.dto;
import jakarta.validation.constraints.*;


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
        private String password;


        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getDoctorGender() {
            return doctorGender;
        }

        public void setDoctorGender(String doctorGender) {
            this.doctorGender = doctorGender;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getDoctorPhoneNumber() {
            return doctorPhoneNumber;
        }

        public void setDoctorPhoneNumber(String doctorPhoneNumber) {
            this.doctorPhoneNumber = doctorPhoneNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
