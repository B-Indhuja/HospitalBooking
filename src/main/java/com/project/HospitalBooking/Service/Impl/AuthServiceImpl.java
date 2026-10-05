package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.Service.AuthService;
import com.project.HospitalBooking.Service.DoctorService;
import com.project.HospitalBooking.Service.PatientService;
import com.project.HospitalBooking.Service.UserService;
import com.project.HospitalBooking.dto.DoctorRegistrationDto;
import com.project.HospitalBooking.dto.LoginRequestDto;
import com.project.HospitalBooking.dto.PatientRegistrationDto;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.Patient;
import com.project.HospitalBooking.entity.User;
import com.project.HospitalBooking.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JwtEncoder jwtEncoder;


    @Autowired
    private PatientService patientService;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;


    @Override
    public String login(LoginRequestDto loginRequest) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .claim(
                        "role",
                        authentication.getAuthorities()
                                .stream()
                                .findFirst()
                                .map(GrantedAuthority::getAuthority)
                                .orElseThrow(() ->
                                        new IllegalStateException("User has no role"))
                )
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }


    @Transactional
    @Override
    public void registerPatient(PatientRegistrationDto request) {

        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.PATIENT);

        User savedUser = userService.createUser(user);

        Patient patient = new Patient();

        patient.setPatientName(request.getPatientName());
        patient.setPatientGender(request.getPatientGender());
        patient.setPatientAge(request.getPatientAge());
        patient.setPatientPhoneNumber(request.getPatientPhoneNumber());
        patient.setUser(savedUser);

        patientService.savePatient(patient);
    }

    @Override
    @Transactional
    public void registerDoctor(DoctorRegistrationDto request) {

        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Create User
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.DOCTOR);

        User savedUser = userService.createUser(user);

        Doctor doctor = new Doctor();

        doctor.setDoctorName(request.getDoctorName());
        doctor.setDoctorGender(request.getDoctorGender());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setDoctorPhoneNumber(request.getDoctorPhoneNumber());
        doctor.setUser(savedUser);

        doctorService.saveDoctor(doctor);
    }
}