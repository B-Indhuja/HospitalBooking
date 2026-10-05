package com.project.HospitalBooking.repository;

import com.project.HospitalBooking.entity.User;
import com.project.HospitalBooking.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(Role role);

}
