package com.project.HospitalBooking.Service.Impl;


import com.project.HospitalBooking.entity.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.HospitalBooking.Service.DoctorService;
import com.project.HospitalBooking.dto.DoctorDto;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService{
    
    @Autowired
    private DoctorRepository doctorRepository;
    private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Override
    public Doctor createDoctor(DoctorDto doctorDto){
        Doctor doctor=new Doctor();
        doctor.setDoctorName(doctorDto.getDoctorName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setDoctorPhoneNumber(doctorDto.getDoctorPhoneNumber());
        doctor.setDoctorGender(doctorDto.getDoctorGender());
        Doctor savedDoctor = doctorRepository.save(doctor);

        log.info("Doctor created successfully with ID: {}",
                savedDoctor.getDoctorId());
        return savedDoctor;
    }

    @Override
    public List<Doctor> getAllDoctors() {

        List<Doctor> doctors = doctorRepository.findAll();
        log.info("Fetched {} doctors", doctors.size());
        return doctors;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public void deactivateDoctor(Integer doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        doctor.setActive(false);
        User user = doctor.getUser();
        if (user != null) {
            user.setActive(false);
        }
        doctorRepository.save(doctor);

        log.info("Doctor {} deactivated successfully", doctorId);
    }

    @Override
    @Transactional
    public void reactivateDoctor(Integer doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        doctor.setActive(true);

        User user = doctor.getUser();
        if (user != null) {
            user.setActive(true);
        }

        doctorRepository.save(doctor);

        log.info("Doctor {} reactivated successfully", doctorId);
    }
}
