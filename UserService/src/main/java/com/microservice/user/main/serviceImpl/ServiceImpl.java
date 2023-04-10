package com.microservice.user.main.serviceImpl;

import com.microservice.user.main.UserRepository.UserRepository;
import com.microservice.user.main.entity.User;
import com.microservice.user.main.exception.ResourceNotFoundException;
import com.microservice.user.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        User createdUser = userRepository.save(user);
        return createdUser;
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("Resource with userId not found : "+userId));
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers;
    }

    @Override
    public void deleteUser(String userId) {

        User deleteUser = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("Resource with userId not found : "+userId));

        userRepository.delete(deleteUser);
    }
}
