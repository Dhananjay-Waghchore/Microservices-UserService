package com.microservice.user.main.service;

import com.microservice.user.main.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    // Create User
    User createUser(User user);

    // Update User
    //User updateUser(String userId);

    // Get user by userId
    User getUserById(String userId);

    // Get all Users
    List<User> getAllUsers();

    // Delete User
    void deleteUser(String userId);

}
