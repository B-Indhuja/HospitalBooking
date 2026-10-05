package com.project.HospitalBooking.scheduler;

import com.project.HospitalBooking.Service.AppointmentService;
import com.project.HospitalBooking.dto.AppointmentResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class AppointmentScheduler {

    @Autowired
    private AppointmentService appointmentService;

    private static final Logger log = LoggerFactory.getLogger(AppointmentScheduler.class);

    @Scheduled(cron = "* * 9 * * *")
    public void generateDailyAppointmentReport() {

        log.info("Scheduler started");
        List<AppointmentResponseDto> appointments = appointmentService.getTodayBookedAppointments();
        log.info("Number of booked appointments: {}", appointments.size());
        generateReport(appointments);
    }
    private void generateReport(List<AppointmentResponseDto> appointments) {

        LocalDate today = LocalDate.now();

        File reportsDirectory = new File("reports");

        if (!reportsDirectory.exists()) {
            reportsDirectory.mkdirs();
        }

        File reportFile = new File(reportsDirectory, "appointments-" + today + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {

            writer.write("DAILY APPOINTMENT REPORT");
            writer.newLine();
            writer.write("Date: " + today);
            writer.newLine();
            writer.newLine();

            if (appointments.isEmpty()) {

                writer.write("No booked appointments for today.");
                writer.newLine();

            } else {

                Map<String, List<AppointmentResponseDto>> doctorWiseAppointments = appointments.stream()
                                .collect(Collectors.groupingBy(
                                        AppointmentResponseDto::getDoctorName
                                ));

                for (Map.Entry<String, List<AppointmentResponseDto>> entry : doctorWiseAppointments.entrySet()) {

                    String doctorName = entry.getKey();
                    List<AppointmentResponseDto> doctorAppointments = entry.getValue();

                    writer.write("----------------------------------------");
                    writer.newLine();

                    writer.write("Doctor: " + doctorName);
                    writer.newLine();

                    writer.write("----------------------------------------");
                    writer.newLine();

                    for (AppointmentResponseDto appointment : doctorAppointments) {
                        writer.write("Appointment ID: " + appointment.getAppointmentId());
                        writer.newLine();
                        writer.write("Patient ID: " + appointment.getPatientId());
                        writer.newLine();
                        writer.write("Shift: " + appointment.getShift());
                        writer.newLine();
                        writer.write("Status: " + appointment.getAppointmentStatus());
                        writer.newLine();
                        writer.newLine();
                    }
                }
            }

            log.info("Daily appointment report created: {}",
                    reportFile.getAbsolutePath());

        } catch (IOException e) {

            log.error("Failed to create daily appointment report", e);
        }
    }
}
