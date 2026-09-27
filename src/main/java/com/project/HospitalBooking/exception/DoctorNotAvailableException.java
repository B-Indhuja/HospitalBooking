package com.project.HospitalBooking.exception;

public class DoctorNotAvailableException extends RuntimeException{
    public DoctorNotAvailableException(String message){
        super(message);
    }
}
