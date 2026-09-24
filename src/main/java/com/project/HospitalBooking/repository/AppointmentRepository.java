package com.project.HospitalBooking.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.HospitalBooking.entity.Appointment;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    boolean existsByPatientAndAppointmentDateAndShiftAndStatusNot(Patient patient, LocalDate appointmentDate, Shift shift, AppointmentStatus status);
    long countByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, LocalDate date, AppointmentStatus appointmentStatus);
}