package com.project.HospitalBooking.Service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;

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
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());

        if(!isValidDate(appointment.getAppointmentDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid appointment date");
        }

        if(!isValidTime(appointment.getAppointmentTime())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid appointment time");
        }

        if(isPastDateTime(appointment.getAppointmentDate(),appointment.getAppointmentTime())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cannot book for a past time");
        }
    
        if(isPatientBooked(appointment.getPatient(),appointment.getAppointmentDate(),appointment.getAppointmentTime())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Patient has already booked another appointment at this time!");
        }

        if(isSlotBooked(appointment.getDoctor(),appointment.getAppointmentDate(),appointment.getAppointmentTime())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Slot is booked already");
        }

        return appointmentRepository.save(appointment);
    }


    private boolean isValidDate(LocalDate date){
        LocalDate today=LocalDate.now();
        if(date.isBefore(today))
            return false;
        if(date.isAfter(today.plusDays(5)))
            return false;
        return true;
    }

    private boolean isValidTime(LocalTime time){
        return (time.isAfter(LocalTime.of(9, 0))&&(time.isBefore(LocalTime.of(12, 0))));
    }

    private boolean isPastDateTime(LocalDate date,LocalTime time){
        LocalDate today=LocalDate.now();
        LocalTime currentTime=LocalTime.now();
        return (date.equals(today)&&time.isBefore(currentTime));
    }

    private boolean isSlotBooked(Doctor doctor,LocalDate date,LocalTime time){
        return appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor,date,time);
    }

    private boolean isPatientBooked(Patient patient,LocalDate date,LocalTime time){
        return appointmentRepository.existsByPatientAndAppointmentDateAndAppointmentTime(patient, date, time);
    }

    @Override
    public void cancelAppointment(Integer appointmentid){
        if(!appointmentRepository.existsById(appointmentid)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Appointment does not exist!");  
        }
        appointmentRepository.deleteById(appointmentid);
    }

}
