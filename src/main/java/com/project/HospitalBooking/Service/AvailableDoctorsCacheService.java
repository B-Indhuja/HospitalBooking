package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.dto.AvailableDoctorDto;

import java.util.List;

public interface AvailableDoctorsCacheService {
    public List<AvailableDoctorDto> getAvailableDoctors();
}
