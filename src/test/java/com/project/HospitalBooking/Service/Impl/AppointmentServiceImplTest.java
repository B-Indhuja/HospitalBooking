package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.dto.AppointmentDto;
import com.project.HospitalBooking.dto.AppointmentResponseDto;
import com.project.HospitalBooking.entity.*;
import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.enums.Shift;
import com.project.HospitalBooking.exception.*;
import com.project.HospitalBooking.repository.AppointmentRepository;
import com.project.HospitalBooking.repository.DoctorAvailabilityRepository;
import com.project.HospitalBooking.repository.DoctorRepository;
import com.project.HospitalBooking.repository.PatientRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void createAppointment_patientNotFound_throwsException() {

        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);

        when(patientRepository.findById(1))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(
                PatientNotFoundException.class,
                () -> appointmentService.createAppointment(dto)
        );

        verify(patientRepository).findById(1);
        verifyNoInteractions(doctorRepository, doctorAvailabilityRepository);
    }


    @Test
    void createAppointment_doctorNotFound_throwsException() {

        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.empty());

        try {
            // Act and Assert
            assertThrows(
                    DoctorNotFoundException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            verify(patientRepository).findById(1);
            verify(doctorRepository).findById(2);

            verifyNoInteractions(doctorAvailabilityRepository);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void createAppointment_wrongPatient_throwsException() {

        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("patient1");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("patient2");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            // Act and Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            assertEquals(
                    "You are not allowed to create an appointment for this patient",
                    exception.getMessage()
            );

            verify(patientRepository).findById(1);
            verifyNoInteractions(doctorRepository);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void createAppointment_pastDate_throwsException() {

        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(LocalDate.now().minusDays(1));

        Patient patient = mock(Patient.class);
        User user = mock(User.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        Doctor doctor = mock(Doctor.class);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        try {
            // Act and Assert
            assertThrows(
                    InvalidAppointmentDateException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            verify(doctorRepository).findById(2);
            verify(appointmentRepository, never()).save(any(Appointment.class));
            verify(appointmentRepository, never()).save(any());

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void createAppointment_patientAlreadyBooked_throwsException() {

        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setShift(Shift.MORNING);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);
        Doctor doctor = mock(Doctor.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        when(appointmentRepository
                .existsByPatientAndAppointmentDateAndShiftAndAppointmentStatusNot(
                        patient,
                        dto.getAppointmentDate(),
                        dto.getShift(),
                        AppointmentStatus.CANCELLED))
                .thenReturn(true);

        try {
            // Act and Assert
            assertThrows(
                    PatientAlreadyBookedException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            verify(appointmentRepository, never()).save(any(Appointment.class));
            verifyNoInteractions(doctorAvailabilityRepository);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void createAppointment_doctorUnavailable_throwsException() {


        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setShift(Shift.MORNING);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);
        Doctor doctor = mock(Doctor.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        when(appointmentRepository
                .existsByPatientAndAppointmentDateAndShiftAndAppointmentStatusNot(
                        patient,
                        dto.getAppointmentDate(),
                        dto.getShift(),
                        AppointmentStatus.CANCELLED))
                .thenReturn(false);

        when(doctorAvailabilityRepository
                .findByDoctorAndAvailabilityDateAndShift(
                        doctor,
                        dto.getAppointmentDate(),
                        dto.getShift()))
                .thenReturn(Optional.empty());

        try {
            // Act and Assert
            assertThrows(
                    DoctorNotAvailableException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            verify(appointmentRepository, never()).save(any(Appointment.class));

            verify(doctorAvailabilityRepository)
                    .findByDoctorAndAvailabilityDateAndShift(
                            doctor,
                            dto.getAppointmentDate(),
                            dto.getShift());

            verify(appointmentRepository, never())
                    .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                            any(Doctor.class),
                            any(LocalDate.class),
                            any(AppointmentStatus.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void createAppointment_doctorFullyBooked_throwsException() {

        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setShift(Shift.MORNING);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);
        Doctor doctor = mock(Doctor.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        when(appointmentRepository
                .existsByPatientAndAppointmentDateAndShiftAndAppointmentStatusNot(
                        patient, dto.getAppointmentDate(), dto.getShift(),
                        AppointmentStatus.CANCELLED))
                .thenReturn(false);

        DoctorAvailability availability = new DoctorAvailability();
        availability.setMaxAppointments(10);

        when(doctorAvailabilityRepository
                .findByDoctorAndAvailabilityDateAndShift(
                        doctor, dto.getAppointmentDate(), dto.getShift()))
                .thenReturn(Optional.of(availability));

        when(appointmentRepository
                .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                        doctor, dto.getAppointmentDate(), AppointmentStatus.CANCELLED))
                .thenReturn(10L);

        try {
            assertThrows(
                    DoctorNotAvailableException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            verify(appointmentRepository, never())
                    .save(any(Appointment.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void createAppointment_todayAfterNineAM_throwsException() {

        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(LocalDate.now());

        Patient patient = mock(Patient.class);
        User user = mock(User.class);

        Doctor doctor = mock(Doctor.class);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            assertThrows(
                    InvalidAppointmentDateException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            verify(doctorRepository).findById(2);
            verify(appointmentRepository, never()).save(any(Appointment.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void createAppointment_todayBeforeNineAM_continuesValidation() {

        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);

        Clock fixedClock = Clock.fixed(
                today.atTime(8, 0).atZone(zone).toInstant(),
                zone
        );

        ReflectionTestUtils.setField(
                appointmentService, "clock", fixedClock
        );

        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(today);
        dto.setShift(Shift.MORNING);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);
        Doctor doctor = mock(Doctor.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        try {
            when(appointmentRepository
                    .existsByPatientAndAppointmentDateAndShiftAndAppointmentStatusNot(
                            patient,
                            today,
                            Shift.MORNING,
                            AppointmentStatus.CANCELLED))
                    .thenReturn(false);

            when(doctorAvailabilityRepository
                    .findByDoctorAndAvailabilityDateAndShift(
                            doctor,
                            today,
                            Shift.MORNING))
                    .thenReturn(Optional.empty());

            assertThrows(
                    DoctorNotAvailableException.class,
                    () -> appointmentService.createAppointment(dto)
            );

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void createAppointment_futureDate_patientAlreadyBooked_throwsException() {

        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setShift(Shift.MORNING);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);
        Doctor doctor = mock(Doctor.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        when(appointmentRepository
                .existsByPatientAndAppointmentDateAndShiftAndAppointmentStatusNot(
                        patient,
                        dto.getAppointmentDate(),
                        dto.getShift(),
                        AppointmentStatus.CANCELLED))
                .thenReturn(true);

        try {
            assertThrows(
                    PatientAlreadyBookedException.class,
                    () -> appointmentService.createAppointment(dto)
            );

            verify(appointmentRepository, never())
                    .save(any(Appointment.class));

            verifyNoInteractions(doctorAvailabilityRepository);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void createAppointment_success() {

        AppointmentDto dto = new AppointmentDto();
        dto.setPatientId(1);
        dto.setDoctorId(2);
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setShift(Shift.MORNING);

        Patient patient = mock(Patient.class);
        User user = mock(User.class);
        Doctor doctor = mock(Doctor.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testpatient");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testpatient");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(doctorRepository.findById(2))
                .thenReturn(Optional.of(doctor));

        when(appointmentRepository
                .existsByPatientAndAppointmentDateAndShiftAndAppointmentStatusNot(
                        patient, dto.getAppointmentDate(), dto.getShift(),
                        AppointmentStatus.CANCELLED))
                .thenReturn(false);

        DoctorAvailability availability = new DoctorAvailability();
        availability.setMaxAppointments(10);

        when(doctorAvailabilityRepository
                .findByDoctorAndAvailabilityDateAndShift(
                        doctor, dto.getAppointmentDate(), dto.getShift()))
                .thenReturn(Optional.of(availability));

        when(appointmentRepository
                .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                        doctor, dto.getAppointmentDate(), AppointmentStatus.CANCELLED))
                .thenReturn(0L);

        when(patient.getPatientId()).thenReturn(1);
        when(doctor.getDoctorId()).thenReturn(2);
        when(doctor.getDoctorName()).thenReturn("Test Doctor");

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> {
                    Appointment appointment = invocation.getArgument(0);
                    appointment.setAppointmentId(100);
                    return appointment;
                });

        try {
            AppointmentResponseDto response =
                    appointmentService.createAppointment(dto);

            assertNotNull(response);
            assertEquals(100, response.getAppointmentId());
            assertEquals(1, response.getPatientId());
            assertEquals(2, response.getDoctorId());
            assertEquals("Test Doctor", response.getDoctorName());
            assertEquals(dto.getAppointmentDate(), response.getAppointmentDate());
            assertEquals(Shift.MORNING, response.getShift());
            assertEquals(AppointmentStatus.BOOKED, response.getAppointmentStatus());

            verify(appointmentRepository).save(any(Appointment.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void cancelAppointment_appointmentNotFound_throwsException() {

        // Arrange
        Integer appointmentId = 1;

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                AppointmentNotFoundException.class,
                () -> appointmentService.cancelAppointment(appointmentId)
        );

        // Verify
        verify(appointmentRepository).findById(appointmentId);

        verify(appointmentRepository, never())
                .save(any(Appointment.class));
    }


    @Test
    void cancelAppointment_wrongPatient_throwsException() {

        // Arrange
        Integer appointmentId = 1;

        Appointment appointment = mock(Appointment.class);
        Patient patient = mock(Patient.class);
        User user = mock(User.class);

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        when(appointment.getPatient()).thenReturn(patient);
        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("patient1");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("patient2");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> appointmentService.cancelAppointment(appointmentId)
            );

            assertEquals(
                    "You are not allowed to cancel this appointment",
                    exception.getMessage()
            );

            // Verify
            verify(appointmentRepository).findById(appointmentId);
            verify(appointmentRepository, never())
                    .save(any(Appointment.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void cancelAppointment_success() {

        // Arrange
        Integer appointmentId = 1;

        Appointment appointment = mock(Appointment.class);
        Patient patient = mock(Patient.class);
        User user = mock(User.class);

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        when(appointment.getPatient()).thenReturn(patient);
        when(patient.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("patient1");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("patient1");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            // Act
            appointmentService.cancelAppointment(appointmentId);

            // Assert
            verify(appointment).setAppointmentStatus(
                    AppointmentStatus.CANCELLED
            );

            verify(appointmentRepository).save(appointment);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void completeAppointment_appointmentNotFound_throwsException() {

        // Arrange
        Integer appointmentId = 1;

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                AppointmentNotFoundException.class,
                () -> appointmentService.completeAppointment(appointmentId)
        );

        // Verify
        verify(appointmentRepository).findById(appointmentId);

        verify(appointmentRepository, never())
                .save(any(Appointment.class));
    }

    @Test
    void completeAppointment_wrongDoctor_throwsException() {

        // Arrange
        Integer appointmentId = 1;

        Appointment appointment = mock(Appointment.class);
        Doctor doctor = mock(Doctor.class);
        User user = mock(User.class);

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        when(appointment.getDoctor()).thenReturn(doctor);
        when(doctor.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("doctor1");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("doctor2");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> appointmentService.completeAppointment(appointmentId)
            );

            assertEquals(
                    "You are not allowed to complete this appointment",
                    exception.getMessage()
            );

            // Verify
            verify(appointmentRepository).findById(appointmentId);

            verify(appointmentRepository, never())
                    .save(any(Appointment.class));

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void completeAppointment_success() {

        // Arrange
        Integer appointmentId = 1;

        Appointment appointment = mock(Appointment.class);
        Doctor doctor = mock(Doctor.class);
        User user = mock(User.class);

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        when(appointment.getDoctor()).thenReturn(doctor);
        when(doctor.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("doctor1");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("doctor1");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            // Act
            appointmentService.completeAppointment(appointmentId);

            // Assert
            verify(appointment).setAppointmentStatus(
                    AppointmentStatus.COMPLETED
            );

            verify(appointmentRepository).save(appointment);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void getAllAppointments_emptyList_returnsEmptyList() {

        // Arrange
        when(appointmentRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        List<AppointmentResponseDto> result =
                appointmentService.getAllAppointments();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(appointmentRepository).findAll();
    }


    @Test
    void getAllAppointments_withAppointments_returnsMappedDtos() {

        // Arrange
        Appointment appointment = mock(Appointment.class);
        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);

        LocalDate appointmentDate = LocalDate.now().plusDays(1);

        when(appointmentRepository.findAll())
                .thenReturn(List.of(appointment));

        when(appointment.getAppointmentId()).thenReturn(1);
        when(appointment.getPatient()).thenReturn(patient);
        when(patient.getPatientId()).thenReturn(10);
        when(appointment.getDoctor()).thenReturn(doctor);
        when(doctor.getDoctorId()).thenReturn(20);
        when(doctor.getDoctorName()).thenReturn("Test Doctor");
        when(appointment.getAppointmentDate()).thenReturn(appointmentDate);
        when(appointment.getShift()).thenReturn(Shift.MORNING);
        when(appointment.getAppointmentStatus())
                .thenReturn(AppointmentStatus.BOOKED);

        // Act
        List<AppointmentResponseDto> result =
                appointmentService.getAllAppointments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        AppointmentResponseDto response = result.get(0);

        assertEquals(1, response.getAppointmentId());
        assertEquals(10, response.getPatientId());
        assertEquals(20, response.getDoctorId());
        assertEquals("Test Doctor", response.getDoctorName());
        assertEquals(appointmentDate, response.getAppointmentDate());
        assertEquals(Shift.MORNING, response.getShift());
        assertEquals(AppointmentStatus.BOOKED, response.getAppointmentStatus());

        verify(appointmentRepository).findAll();
    }

    @Test
    void getTodayBookedAppointments_emptyList_returnsEmptyList() {

        // Arrange
        LocalDate today = LocalDate.now();

        when(appointmentRepository.findByAppointmentDateAndAppointmentStatus(
                today, AppointmentStatus.BOOKED))
                .thenReturn(Collections.emptyList());

        // Act
        List<AppointmentResponseDto> result =
                appointmentService.getTodayBookedAppointments();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(appointmentRepository)
                .findByAppointmentDateAndAppointmentStatus(
                        today, AppointmentStatus.BOOKED);
    }

    @Test
    void getTodayBookedAppointments_withAppointments_returnsMappedDtos() {

        // Arrange
        LocalDate today = LocalDate.now();

        Appointment appointment = mock(Appointment.class);
        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);

        when(appointmentRepository.findByAppointmentDateAndAppointmentStatus(
                today, AppointmentStatus.BOOKED))
                .thenReturn(List.of(appointment));

        when(appointment.getAppointmentId()).thenReturn(1);
        when(appointment.getPatient()).thenReturn(patient);
        when(patient.getPatientId()).thenReturn(10);
        when(appointment.getDoctor()).thenReturn(doctor);
        when(doctor.getDoctorId()).thenReturn(20);
        when(doctor.getDoctorName()).thenReturn("Test Doctor");
        when(appointment.getAppointmentDate()).thenReturn(today);
        when(appointment.getShift()).thenReturn(Shift.MORNING);
        when(appointment.getAppointmentStatus())
                .thenReturn(AppointmentStatus.BOOKED);

        // Act
        List<AppointmentResponseDto> result =
                appointmentService.getTodayBookedAppointments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        AppointmentResponseDto response = result.get(0);

        assertEquals(1, response.getAppointmentId());
        assertEquals(10, response.getPatientId());
        assertEquals(20, response.getDoctorId());
        assertEquals("Test Doctor", response.getDoctorName());
        assertEquals(today, response.getAppointmentDate());
        assertEquals(Shift.MORNING, response.getShift());
        assertEquals(AppointmentStatus.BOOKED, response.getAppointmentStatus());

        verify(appointmentRepository)
                .findByAppointmentDateAndAppointmentStatus(
                        today, AppointmentStatus.BOOKED);
    }


}
