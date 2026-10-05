package com.project.HospitalBooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name="patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
    
    @Column(nullable = false,length = 30)
    private String patientName;

    @Column(nullable = false)
    private String patientGender;

    @Column(nullable = false)
    private Integer patientAge;

    @Column(nullable = false,length = 10,unique = true)
    private String patientPhoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public Integer getPatientId() {
        return patientId;
    }

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

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String gender) {
        this.patientGender = gender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
