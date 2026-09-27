package com.project.HospitalBooking.dto;

public class DoctorDto{
    private String doctorName;
    private String doctorGender;
    private String specialization;
    private String doctorPhoneNumber;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getDoctorGender() {
        return doctorGender;
    }

    public void setDoctorGender(String gender) {
        this.doctorGender = gender;
    }
}