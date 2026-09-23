package com.project.HospitalBooking.dto;

public class PatientDto {
    private String patientName;
    private Integer patientAge;    
    private String patientPhoneNumber;

     public String getPatientName() {
         return patientName;
    }

    public void setPatientName(String patientName) {
         this.patientName = patientName;
    }

    public Integer getPatientAge() {
         return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
         this.patientAge = patientAge;
    }

    public String getPatientPhoneNumber() {
         return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
         this.patientPhoneNumber = patientPhoneNumber;
    }
    
}