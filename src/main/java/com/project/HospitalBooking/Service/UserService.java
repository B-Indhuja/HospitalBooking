package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.entity.User;
import com.project.HospitalBooking.enums.Role;

public interface UserService {
    User createUser(User user);
    boolean existsByUsername(String username);
    boolean existsByRole(Role role);
}
