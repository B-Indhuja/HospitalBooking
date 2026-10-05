package com.project.HospitalBooking.Service;

import com.project.HospitalBooking.entity.User;

public interface UserService {
    User createUser(User user);
    boolean existsByUsername(String username);
}
