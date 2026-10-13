package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.Service.AvailableDoctorsCacheService;
import com.project.HospitalBooking.dto.AvailableDoctorDto;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.DoctorAvailability;
import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.repository.AppointmentRepository;
import com.project.HospitalBooking.repository.DoctorAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailableDoctorsCacheServiceImpl implements AvailableDoctorsCacheService {

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Cacheable("availableDoctors")
    @Override
    public List<AvailableDoctorDto> getAvailableDoctors() {

        System.out.println("Loading available doctors from database");

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

            // Ignore fully booked doctors
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
