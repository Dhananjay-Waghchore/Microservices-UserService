package com.microservice.user.main.controller;

import com.microservice.user.main.entity.User;
import com.microservice.user.main.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User createdUser = this.userService.createUser(user);
        return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
    }

    int retryCount = 1;
    @GetMapping("/{userId}")
    //@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    //@Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUserById(@PathVariable String userId){
        logger.info("retryCount : {} ",retryCount);
        User userById = this.userService.getUserById(userId);
        return new  ResponseEntity<User>(userById, HttpStatus.FOUND);
    }

    // Creating fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
        logger.info("fallback method is executed service is down", ex.getMessage());
        User user = User.builder()
                        .userName("dummy")
                        .password("password")
                        .email("dummy@gmail.com")
                        .userId("12345")
                        .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allUsers = this.userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable String userId){
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
