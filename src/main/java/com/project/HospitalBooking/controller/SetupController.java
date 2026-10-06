package com.project.HospitalBooking.controller;
import com.project.HospitalBooking.dto.AdminSetupDto;
import com.project.HospitalBooking.entity.User;
import com.project.HospitalBooking.enums.Role;
import com.project.HospitalBooking.Service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/setup")
public class SetupController {


    @Autowired
    private UserService userService;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(SetupController.class);

    @PostMapping("/admin")
    public ResponseEntity<String> createAdmin(
           @Valid @RequestBody AdminSetupDto request) {

        if (userService.existsByRole(Role.ADMIN)) {

            logger.warn(
                    "Admin bootstrap attempted, but an admin already exists. Username: {}",
                    request.getUsername()
            );

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin bootstrap has already been completed");
        }

        if (userService.existsByUsername(request.getUsername())) {

            logger.warn(
                    "Admin bootstrap failed because username already exists: {}",
                    request.getUsername()
            );

            return ResponseEntity
                    .badRequest()
                    .body("Username already exists");
        }

        User admin = new User();

        admin.setUsername(request.getUsername());
        admin.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        admin.setRole(Role.ADMIN);

        userService.createUser(admin);

        logger.info(
                "Initial admin user created successfully. Username: {}",
                request.getUsername()
        );

        return ResponseEntity.ok("Admin created successfully");
    }
    }

