package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.Service.DoctorAvailabilityService;
import com.project.HospitalBooking.dto.AvailableDoctorDto;
import com.project.HospitalBooking.dto.DoctorAvailabilityDto;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.DoctorAvailability;
import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.exception.DoctorAvailabilityAlreadyExistsException;
import com.project.HospitalBooking.exception.DoctorNotFoundException;
import com.project.HospitalBooking.repository.AppointmentRepository;
import com.project.HospitalBooking.repository.DoctorAvailabilityRepository;
import com.project.HospitalBooking.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private static final Logger log = LoggerFactory.getLogger(DoctorAvailabilityServiceImpl.class);

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public DoctorAvailability addAvailability(DoctorAvailabilityDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found"
                ));
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"))) {

            if (!doctor.getUser().getUsername().equals(username)) {
                throw new RuntimeException(
                        "You are not allowed to create availability for this doctor"
                );
            }
        }
        if (doctorAvailabilityRepository
                .existsByDoctorAndAvailabilityDateAndShift(
                        doctor,
                        dto.getAvailabilityDate(),
                        dto.getShift())) {
            throw new DoctorAvailabilityAlreadyExistsException(
                    "Doctor availability already exists for this date and shift"
            );
        }
        DoctorAvailability availability = new DoctorAvailability();

        availability.setDoctor(doctor);
        availability.setAvailabilityDate(dto.getAvailabilityDate());
        availability.setShift(dto.getShift());
        availability.setMaxAppointments(dto.getMaxAppointments());
        DoctorAvailability savedAvailability =
                doctorAvailabilityRepository.save(availability);

        log.info("Doctor availability added successfully for doctor {} on {} for {} shift",
                doctor.getDoctorId(),
                dto.getAvailabilityDate(),
                dto.getShift());

        return savedAvailability;
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<DoctorAvailabilityDto> getAllDoctorAvailability() {

        List<DoctorAvailability> availabilityList = doctorAvailabilityRepository.findAll();

        List<DoctorAvailabilityDto> dtoList = new ArrayList<>();

        for (DoctorAvailability availability : availabilityList) {

            DoctorAvailabilityDto dto = new DoctorAvailabilityDto();

            dto.setDoctorId(availability.getDoctor().getDoctorId());
            dto.setAvailabilityDate(availability.getAvailabilityDate());
            dto.setShift(availability.getShift());
            dto.setMaxAppointments(availability.getMaxAppointments());

            dtoList.add(dto);
        }

        log.info("Fetched {} doctor availability records",
                availabilityList.size());
        return dtoList;
    }

    @Override
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    @Cacheable("availableDoctors")
    public List<AvailableDoctorDto> getAvailableDoctors() {
        System.out.println("Executing getAvailableDoctors()");
        LocalDate today = LocalDate.now();

        List<DoctorAvailability> availabilities =
                doctorAvailabilityRepository
                        .findByAvailabilityDateGreaterThanEqual(today);

        List<AvailableDoctorDto> availableDoctors = new ArrayList<>();

        for (DoctorAvailability availability : availabilities) {

            Doctor doctor = availability.getDoctor();

            // Ignore inactive doctors
            if (!doctor.isActive()) {
                continue;
            }

            long bookedAppointments =
                    appointmentRepository
                            .countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                                    doctor,
                                    availability.getAvailabilityDate(),
                                    AppointmentStatus.CANCELLED
                            );

            int remainingSlots =
                    availability.getMaxAppointments()
                            - (int) bookedAppointments;

            // Ignore doctors with no remaining slots
            if (remainingSlots <= 0) {
                continue;
            }

            AvailableDoctorDto dto = new AvailableDoctorDto();

            dto.setDoctorId(doctor.getDoctorId());
            dto.setDoctorName(doctor.getDoctorName());
            dto.setSpecialization(doctor.getSpecialization());
            dto.setAvailabilityDate(availability.getAvailabilityDate());
            dto.setShift(availability.getShift());
            dto.setRemainingSlots(remainingSlots);

            availableDoctors.add(dto);
        }

        return availableDoctors;
    }
}
