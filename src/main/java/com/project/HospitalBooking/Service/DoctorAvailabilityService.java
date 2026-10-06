package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.dto.AvailableDoctorDto;
import com.project.HospitalBooking.dto.DoctorAvailabilityDto;
import com.project.HospitalBooking.entity.DoctorAvailability;

import java.util.List;

public interface DoctorAvailabilityService {
    DoctorAvailability addAvailability(DoctorAvailabilityDto dto);
    List<DoctorAvailabilityDto> getAllDoctorAvailability();
    List<AvailableDoctorDto> getAvailableDoctors();
}
