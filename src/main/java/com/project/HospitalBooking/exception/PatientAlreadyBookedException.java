package com.project.HospitalBooking.exception;

public class PatientAlreadyBookedException extends RuntimeException{
    public PatientAlreadyBookedException(String message){
        super(message);
    }
}
