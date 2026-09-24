package com.project.HospitalBooking.repository;

import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.DoctorAvailability;
import com.project.HospitalBooking.enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability,Integer> {
    boolean existsByDoctorAndAvailabilityDateAndShift(Doctor doctor, LocalDate date, Shift shift);

}
