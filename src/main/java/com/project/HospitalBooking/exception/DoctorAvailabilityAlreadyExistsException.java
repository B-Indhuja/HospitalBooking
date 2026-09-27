package com.project.HospitalBooking.exception;

public class DoctorAvailabilityAlreadyExistsException extends RuntimeException{
    public DoctorAvailabilityAlreadyExistsException(String message){
        super(message);
    }
}
