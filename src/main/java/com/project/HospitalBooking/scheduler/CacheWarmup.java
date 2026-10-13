package com.project.HospitalBooking.scheduler;
import com.project.HospitalBooking.Service.AvailableDoctorsCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheWarmup {

    @Autowired
    private  AvailableDoctorsCacheService availableDoctorsCacheService;

    @EventListener(ApplicationReadyEvent.class)
    public void warmUpCache() {

        log.info("Starting available doctors cache warmup");

        availableDoctorsCacheService.getAvailableDoctors();

        log.info("Available doctors cache warmup completed");
    }

}
