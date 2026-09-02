package com.project.HospitalBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.HospitalBooking.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {

    
}