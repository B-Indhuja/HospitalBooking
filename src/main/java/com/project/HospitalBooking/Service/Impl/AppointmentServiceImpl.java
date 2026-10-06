package com.project.HospitalBooking.Service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project.HospitalBooking.dto.AppointmentResponseDto;
import com.project.HospitalBooking.entity.DoctorAvailability;
import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.enums.Shift;
import com.project.HospitalBooking.exception.*;
import com.project.HospitalBooking.repository.DoctorAvailabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import com.project.HospitalBooking.Service.AppointmentService;
import com.project.HospitalBooking.dto.AppointmentDto;
import com.project.HospitalBooking.entity.Appointment;
import com.project.HospitalBooking.entity.Doctor;
import com.project.HospitalBooking.entity.Patient;
import com.project.HospitalBooking.repository.AppointmentRepository;
import com.project.HospitalBooking.repository.DoctorRepository;
import com.project.HospitalBooking.repository.PatientRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Override
    @PreAuthorize("hasRole('PATIENT')")
    public AppointmentResponseDto createAppointment(AppointmentDto appointmentDto){

        Patient patient = patientRepository.findById(appointmentDto.getPatientId())
                                           .orElseThrow(() ->new PatientNotFoundException("Patient not found"));
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        if (!patient.getUser().getUsername().equals(username)) {
            throw new RuntimeException(
                    "You are not allowed to create an appointment for this patient"
            );
        }

        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                                        .orElseThrow(() ->new DoctorNotFoundException("Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setShift(appointmentDto.getShift());
        appointment.setAppointmentStatus(AppointmentStatus.BOOKED);
       /*
        if(!isValidDate(appointment.getAppointmentDate())){
            throw new InvalidAppointmentDateException("Invalid appointment date");
        }

        */
    
        if(isPatientBooked(appointment.getPatient(),appointment.getAppointmentDate(),appointment.getShift())){
            throw new PatientAlreadyBookedException("Patient has already booked another appointment at this time!");
        }

        if (!isDoctorAvailable(appointment.getDoctor(),appointment.getAppointmentDate(),appointment.getShift())) {
            throw new DoctorNotAvailableException(
                    "Doctor is not available or has reached the maximum number of appointments"
            );
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment {} created successfully for patient {} with doctor {}",
                savedAppointment.getAppointmentId(),
                patient.getPatientId(),
                doctor.getDoctorId());


        AppointmentResponseDto responseDto=new AppointmentResponseDto();
        responseDto.setAppointmentId(savedAppointment.getAppointmentId());
        responseDto.setPatientId(savedAppointment.getPatient().getPatientId());
        responseDto.setDoctorId(savedAppointment.getDoctor().getDoctorId());
        responseDto.setDoctorName(savedAppointment.getDoctor().getDoctorName());
        responseDto.setAppointmentDate(savedAppointment.getAppointmentDate());
        responseDto.setAppointmentStatus(savedAppointment.getAppointmentStatus());
        responseDto.setShift(savedAppointment.getShift());

        return responseDto;
    }


    private boolean isValidDate(LocalDate date){
        LocalDate today=LocalDate.now();
        if(date.isBefore(today))
            return false;
        if (date.equals(today)) {
            LocalTime currentTime = LocalTime.now();
            if (!currentTime.isBefore(LocalTime.of(9, 0))) {
                return false;
            }
        }
        return true;
    }

    private boolean isPatientBooked(Patient patient, LocalDate date, Shift shift){
        return appointmentRepository.existsByPatientAndAppointmentDateAndShiftAndAppointmentStatusNot(patient, date, shift,AppointmentStatus.CANCELLED);
    }

    private boolean isDoctorAvailable(Doctor doctor,LocalDate date,Shift shift){
        Optional<DoctorAvailability> availability = doctorAvailabilityRepository.findByDoctorAndAvailabilityDateAndShift(doctor, date, shift);
        if (availability.isEmpty()) {
            return false;
        }
        long appointmentCount = appointmentRepository.countByDoctorAndAppointmentDateAndAppointmentStatusNot(
                        doctor, date, AppointmentStatus.CANCELLED);

        return appointmentCount < availability.get().getMaxAppointments();
    }

    @Override
    @PreAuthorize("hasRole('PATIENT')")
    public void cancelAppointment(Integer appointmentId){
        Appointment appointment=appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment does not exist!"
                        ));
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        if (!appointment.getPatient().getUser().getUsername().equals(username)) {
            throw new RuntimeException(
                    "You are not allowed to cancel this appointment"
            );
        }
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        log.info("Appointment {} cancelled successfully", appointmentId);
    }

    @Override
    @PreAuthorize("hasRole('DOCTOR')")
    public void completeAppointment(Integer appointmentId){
        Appointment appointment=appointmentRepository.findById(appointmentId)
                .orElseThrow(()->new AppointmentNotFoundException(
                        "Appointment does not exist!"
                ));
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        if (!appointment.getDoctor().getUser().getUsername().equals(username)) {
            throw new RuntimeException(
                    "You are not allowed to complete this appointment"
            );
        }
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        log.info("Appointment {} completed successfully", appointmentId);

    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppointmentResponseDto> getAllAppointments() {

        List<Appointment> appointments = appointmentRepository.findAll();

        List<AppointmentResponseDto> dtoList = new ArrayList<>();

        for (Appointment appointment : appointments) {

            AppointmentResponseDto dto = new AppointmentResponseDto();

            dto.setPatientId(appointment.getPatient().getPatientId());
            dto.setDoctorId(appointment.getDoctor().getDoctorId());
            dto.setAppointmentDate(appointment.getAppointmentDate());
            dto.setShift(appointment.getShift());
            dto.setDoctorName(appointment.getDoctor().getDoctorName());
            dto.setAppointmentStatus(appointment.getAppointmentStatus());
            dto.setAppointmentId(appointment.getAppointmentId());

            dtoList.add(dto);
        }
        log.info("Fetched {} appointments", dtoList.size());
        return dtoList;
    }
    @Override
    public List<AppointmentResponseDto> getTodayBookedAppointments() {

        List<Appointment> appointments =
                appointmentRepository.findByAppointmentDateAndAppointmentStatus(
                        LocalDate.now(),
                        AppointmentStatus.BOOKED);

        List<AppointmentResponseDto> dtoList = new ArrayList<>();

        for (Appointment appointment : appointments) {

            AppointmentResponseDto dto = new AppointmentResponseDto();

            dto.setAppointmentId(appointment.getAppointmentId());
            dto.setPatientId(appointment.getPatient().getPatientId());
            dto.setDoctorId(appointment.getDoctor().getDoctorId());
            dto.setDoctorName(appointment.getDoctor().getDoctorName());
            dto.setAppointmentDate(appointment.getAppointmentDate());
            dto.setShift(appointment.getShift());
            dto.setAppointmentStatus(appointment.getAppointmentStatus());

            dtoList.add(dto);
        }

        log.info("Fetched {} booked appointments for today", dtoList.size());

        return dtoList;
    }
}
