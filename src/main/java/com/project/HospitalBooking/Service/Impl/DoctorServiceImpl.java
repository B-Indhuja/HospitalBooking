package com.project.HospitalBooking.Service.Impl;

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

    @Override
    public Doctor createDoctor(DoctorDto doctorDto){
        Doctor doctor=new Doctor();
        doctor.setDoctorName(doctorDto.getDoctorName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setDoctorPhoneNumber(doctorDto.getDoctorPhoneNumber());
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
