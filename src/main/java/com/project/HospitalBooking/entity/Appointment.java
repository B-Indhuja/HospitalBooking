package com.project.HospitalBooking.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.enums.Shift;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "appointmentSequence")
    @SequenceGenerator(name = "appointmentSequence",sequenceName = "appointmentSeq",initialValue = 101)
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name="doctor_id",nullable = false)
    private Doctor doctor;
    
    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Shift shift;
    


}
