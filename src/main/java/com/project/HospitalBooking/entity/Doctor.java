package com.project.HospitalBooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name="doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq1")
    @SequenceGenerator(name = "seq1",sequenceName = "seq1",allocationSize = 1)
    private Integer doctorId;

    @Column(nullable=false,length=30)
    private String doctorName;

    @Column(nullable=false,length=50)
    private String specialization;

    @Column(nullable=false,length=10,unique = true)
    private String doctorPhoneNumber;

    public Integer getDoctorId() {
        return doctorId;
    }

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
    

}
