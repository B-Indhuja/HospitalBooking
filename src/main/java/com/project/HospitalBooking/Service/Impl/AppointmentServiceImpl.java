package com.project.HospitalBooking.Service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;

import com.project.HospitalBooking.enums.AppointmentStatus;
import com.project.HospitalBooking.enums.Shift;
import com.project.HospitalBooking.repository.DoctorAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public Appointment createAppointment(AppointmentDto appointmentDto){

        Patient patient = patientRepository.findById(appointmentDto.getPatientId())
                                           .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND,"Patient not found"));
        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                                        .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND,"Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setShift(appointmentDto.getShift());
        appointment.setAppointmentStatus(AppointmentStatus.BOOKED);

        if(!isValidDate(appointment.getAppointmentDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid appointment date");
        }
    
        if(isPatientBooked(appointment.getPatient(),appointment.getAppointmentDate(),appointment.getShift())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Patient has already booked another appointment at this time!");
        }

        if (!isDoctorAvailable(appointment.getDoctor(),appointment.getAppointmentDate(),appointment.getShift())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Doctor is not available or has reached the maximum of 30 appointments"
            );
        }

        return appointmentRepository.save(appointment);
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
        return appointmentRepository.existsByPatientAndAppointmentDateAndShiftAndStatusNot(patient, date, shift,AppointmentStatus.CANCELLED);
    }

    private boolean isDoctorAvailable(Doctor doctor,LocalDate date,Shift shift){
        boolean available = doctorAvailabilityRepository.existsByDoctorAndAvailabilityDateAndShift(doctor, date, shift);
        if (!available) {
            return false;
        }
        long appointmentCount = appointmentRepository.countByDoctorAndAppointmentDateAndStatusNot(doctor, date, AppointmentStatus.CANCELLED);
        return appointmentCount < 30;
    }

    @Override
    public void cancelAppointment(Integer appointmentId){
        Appointment appointment=appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Appointment does not exist!"
                        ));
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public void completeAppointment(Integer appointmentId){
        Appointment appointment=appointmentRepository.findById(appointmentId)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Appointment does not exist!"
                ));
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

}
