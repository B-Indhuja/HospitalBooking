package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.dto.DoctorRegistrationDto;
import com.project.HospitalBooking.dto.LoginRequestDto;
import com.project.HospitalBooking.dto.PatientRegistrationDto;

public interface AuthService {
    String login(LoginRequestDto loginRequest);
    void registerPatient(PatientRegistrationDto request);
    void registerDoctor(DoctorRegistrationDto request);
}
