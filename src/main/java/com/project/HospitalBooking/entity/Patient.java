package com.project.HospitalBooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="patients")
@Getter
@Setter
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

    @Column(nullable = false)
    private boolean active = true;

}
