package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.Service.DoctorAvailabilityService;
import com.project.HospitalBooking.dto.DoctorAvailabilityDto;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.DoctorAvailability;
import com.project.HospitalBooking.exception.DoctorAvailabilityAlreadyExistsException;
import com.project.HospitalBooking.exception.DoctorNotFoundException;
import com.project.HospitalBooking.repository.DoctorAvailabilityRepository;
import com.project.HospitalBooking.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    private static final Logger log = LoggerFactory.getLogger(DoctorAvailabilityServiceImpl.class);

    @Override
    public DoctorAvailability addAvailability(DoctorAvailabilityDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found"
                ));

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
}
