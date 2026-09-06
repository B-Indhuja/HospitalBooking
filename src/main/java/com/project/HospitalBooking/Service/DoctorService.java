package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.dto.DoctorDto;
import com.project.HospitalBooking.entity.Doctor;

import java.util.List;

public interface DoctorService {
    Doctor createDoctor(DoctorDto doctorDto);
    List<Doctor> getAllDoctors();
}
