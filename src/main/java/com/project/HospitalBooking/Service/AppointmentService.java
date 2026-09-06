package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.dto.AppointmentDto;
import com.project.HospitalBooking.entity.Appointment;

public interface AppointmentService {
    Appointment createAppointment(AppointmentDto appointmentDto);
    void cancelAppointment(Integer appointmentId);
    void completeAppointment(Integer appointmentId);
}
