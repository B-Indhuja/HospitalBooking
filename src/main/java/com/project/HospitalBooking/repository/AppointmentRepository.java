package com.project.HospitalBooking.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.HospitalBooking.entity.Appointment;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(Doctor doctor,LocalDate appointmentDate,LocalTime appointmentTime);
    boolean existsByPatientAndAppointmentDateAndAppointmentTime(Patient patient,LocalDate appointmentDate,LocalTime appointmentTime);
}