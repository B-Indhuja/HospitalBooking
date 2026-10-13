package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.dto.AvailableDoctorDto;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.DoctorAvailability;
import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.repository.AppointmentRepository;
import com.project.HospitalBooking.repository.DoctorAvailabilityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailableDoctorsCacheServiceImplTest {
    @Mock
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AvailableDoctorsCacheServiceImpl availableDoctorsCacheService;


    @Test
    void getAvailableDoctors_noAvailabilities_returnsEmptyList() {

        when(doctorAvailabilityRepository
                .findByAvailabilityDateGreaterThanEqual(any(LocalDate.class)))
                .thenReturn(List.of());

        List<AvailableDoctorDto> result =
                availableDoctorsCacheService.getAvailableDoctors();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(doctorAvailabilityRepository)
                .findByAvailabilityDateGreaterThanEqual(any(LocalDate.class));

        verifyNoInteractions(appointmentRepository);
    }


    @Test
    void getAvailableDoctors_inactiveDoctor_skipsDoctor() {

        Doctor doctor = mock(Doctor.class);
        DoctorAvailability availability = mock(DoctorAvailability.class);

        when(doctorAvailabilityRepository
                .findByAvailabilityDateGreaterThanEqual(any(LocalDate.class)))
                .thenReturn(List.of(availability));

        when(availability.getDoctor()).thenReturn(doctor);
        when(doctor.isActive()).thenReturn(false);

        List<AvailableDoctorDto> result =
                availableDoctorsCacheService.getAvailableDoctors();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(doctor).isActive();
        verify(appointmentRepository, never())
                .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                        any(Doctor.class),
                        any(LocalDate.class),
                        any(AppointmentStatus.class)
                );
    }

    @Test
    void getAvailableDoctors_fullyBookedDoctor_skipsDoctor() {

        Doctor doctor = mock(Doctor.class);
        DoctorAvailability availability = mock(DoctorAvailability.class);

        when(doctorAvailabilityRepository
                .findByAvailabilityDateGreaterThanEqual(any(LocalDate.class)))
                .thenReturn(List.of(availability));

        when(availability.getDoctor()).thenReturn(doctor);
        when(doctor.isActive()).thenReturn(true);

        when(availability.getAvailabilityDate())
                .thenReturn(LocalDate.now().plusDays(1));

        when(availability.getMaxAppointments()).thenReturn(10);

        when(appointmentRepository
                .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                        eq(doctor),
                        any(LocalDate.class),
                        eq(AppointmentStatus.CANCELLED)
                ))
                .thenReturn(10L);

        List<AvailableDoctorDto> result =
                availableDoctorsCacheService.getAvailableDoctors();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(appointmentRepository)
                .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                        eq(doctor),
                        any(LocalDate.class),
                        eq(AppointmentStatus.CANCELLED)
                );
    }

    @Test
    void getAvailableDoctors_doctorHasRemainingSlots_includesDoctor() {

        Doctor doctor = mock(Doctor.class);
        DoctorAvailability availability = mock(DoctorAvailability.class);

        LocalDate availabilityDate = LocalDate.now().plusDays(1);

        when(doctorAvailabilityRepository
                .findByAvailabilityDateGreaterThanEqual(any(LocalDate.class)))
                .thenReturn(List.of(availability));

        when(availability.getDoctor()).thenReturn(doctor);
        when(doctor.isActive()).thenReturn(true);

        when(appointmentRepository
                .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                        eq(doctor),
                        eq(availabilityDate),
                        eq(AppointmentStatus.CANCELLED)
                ))
                .thenReturn(3L);

        when(availability.getAvailabilityDate()).thenReturn(availabilityDate);
        when(availability.getMaxAppointments()).thenReturn(10);
        when(doctor.getDoctorId()).thenReturn(2);
        when(doctor.getDoctorName()).thenReturn("Test Doctor");
        when(doctor.getSpecialization()).thenReturn("Cardiology");

        List<AvailableDoctorDto> result =
                availableDoctorsCacheService.getAvailableDoctors();

        assertEquals(1, result.size());

        AvailableDoctorDto dto = result.get(0);
        assertEquals(2, dto.getDoctorId());
        assertEquals("Test Doctor", dto.getDoctorName());
        assertEquals("Cardiology", dto.getSpecialization());
        assertEquals(7, dto.getRemainingSlots());
    }

}
