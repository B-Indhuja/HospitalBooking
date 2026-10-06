package com.project.HospitalBooking.entity;
import com.project.HospitalBooking.enums.Shift;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="doctor_availability",uniqueConstraints = {@UniqueConstraint(columnNames = {"doctor_id", "availability_date", "shift"})})
@Getter
@Setter
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer availabilityId;

    @ManyToOne
    @JoinColumn(name="doctor_id",nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate availabilityDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Shift shift;

    @Column(nullable = false)
    private Integer maxAppointments;

}
