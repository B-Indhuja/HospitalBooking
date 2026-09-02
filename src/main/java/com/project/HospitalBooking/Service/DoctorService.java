package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.dto.DoctorDto;
import com.project.HospitalBooking.entity.Doctor;

public interface DoctorService {
    Doctor createDoctor(DoctorDto doctorDto);
}
