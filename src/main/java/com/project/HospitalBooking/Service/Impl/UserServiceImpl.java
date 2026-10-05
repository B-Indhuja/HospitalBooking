package com.project.HospitalBooking.Service.Impl;

import com.project.HospitalBooking.Service.UserService;
import com.project.HospitalBooking.entity.User;
import com.project.HospitalBooking.enums.Role;
import com.project.HospitalBooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByRole(Role role) {
        return userRepository.existsByRole(role);
    }
}
