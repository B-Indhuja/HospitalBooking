
package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.Service.DoctorService;
import com.project.HospitalBooking.Service.PatientService;
import com.project.HospitalBooking.Service.UserService;
import com.project.HospitalBooking.dto.DoctorRegistrationDto;
import com.project.HospitalBooking.dto.LoginRequestDto;

import com.project.HospitalBooking.dto.PatientRegistrationDto;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.Patient;
import com.project.HospitalBooking.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private PatientService patientService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private AuthServiceImpl authService;

    private void testLogin(String username, String role, String expectedToken) {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername(username);
        request.setPassword("password123");

        Authentication authentication = mock(Authentication.class);

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getName()).thenReturn(username);

        doReturn(java.util.Collections.singletonList(authority))
                .when(authentication).getAuthorities();

        Jwt jwt = mock(Jwt.class);

        when(jwt.getTokenValue()).thenReturn(expectedToken);

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        String token = authService.login(request);

        assertEquals(expectedToken, token);
    }

    @Test
    void login_admin_returnsJwtToken() {
        testLogin("admin", "ROLE_ADMIN", "admin-jwt");
    }

    @Test
    void login_patient_returnsJwtToken() {
        testLogin("patient", "ROLE_PATIENT", "patient-jwt");
    }

    @Test
    void login_doctor_returnsJwtToken() {
        testLogin("doctor", "ROLE_DOCTOR", "doctor-jwt");
    }


    @Test
    void login_userWithNoRole_throwsIllegalStateException() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("user");
        request.setPassword("password123");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getName()).thenReturn("user");

        doReturn(java.util.Collections.emptyList())
                .when(authentication).getAuthorities();

        assertThrows(
                IllegalStateException.class,
                () -> authService.login(request)
        );
    }


    @Test
    void registerPatient_success() {
        PatientRegistrationDto request = new PatientRegistrationDto();
        request.setUsername("patient1");
        request.setPassword("password123");
        request.setPatientName("Test Patient");
        request.setPatientGender("Male");
        request.setPatientAge(25);
        request.setPatientPhoneNumber("9876543210");

        when(userService.existsByUsername("patient1"))
                .thenReturn(false);
        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        User savedUser = new User();
        when(userService.createUser(any(User.class)))
                .thenReturn(savedUser);

        authService.registerPatient(request);

        verify(userService).createUser(any(User.class));
        verify(patientService).savePatient(any(Patient.class));
    }

    @Test
    void registerPatient_usernameAlreadyExists_throwsException() {
        PatientRegistrationDto request = new PatientRegistrationDto();
        request.setUsername("patient1");

        when(userService.existsByUsername("patient1"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.registerPatient(request)
        );

        assertEquals("Username already exists", exception.getMessage());

        verify(userService, never()).createUser(any(User.class));
        verify(patientService, never()).savePatient(any(Patient.class));
    }

    @Test
    void registerDoctor_success() {
        DoctorRegistrationDto request = new DoctorRegistrationDto();
        request.setUsername("doctor1");
        request.setPassword("password123");
        request.setDoctorName("Test Doctor");
        request.setDoctorGender("Male");
        request.setSpecialization("Cardiology");
        request.setDoctorPhoneNumber("9876543210");

        when(userService.existsByUsername("doctor1"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        User savedUser = new User();

        when(userService.createUser(any(User.class)))
                .thenReturn(savedUser);

        authService.registerDoctor(request);

        verify(userService).createUser(any(User.class));
        verify(doctorService).saveDoctor(any(Doctor.class));
    }

    @Test
    void registerDoctor_usernameAlreadyExists_throwsException() {
        DoctorRegistrationDto request = new DoctorRegistrationDto();
        request.setUsername("doctor1");

        when(userService.existsByUsername("doctor1"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.registerDoctor(request)
        );

        assertEquals("Username already exists", exception.getMessage());

        verify(userService, never()).createUser(any(User.class));
        verify(doctorService, never()).saveDoctor(any(Doctor.class));
    }

}
