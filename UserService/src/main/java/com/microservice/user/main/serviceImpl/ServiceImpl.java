package com.microservice.user.main.serviceImpl;

import com.microservice.user.main.UserRepository.UserRepository;
import com.microservice.user.main.entity.Hotel;
import com.microservice.user.main.entity.Rating;
import com.microservice.user.main.entity.User;
import com.microservice.user.main.exception.ResourceNotFoundException;
import com.microservice.user.main.external.service.HotelService;
import com.microservice.user.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Override
    public User createUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        User createdUser = userRepository.save(user);
        return createdUser;
    }

    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("Resource with userId not found : "+userId));

        //Fetch rating of above user from rating service
        Rating[] userRating = restTemplate.getForObject("http://RatingService/rating/"+user.getUserId(), Rating[].class);
        List<Rating> listRating = Arrays.stream(userRating).toList();

        //Fetch hotel information for above rating
        listRating.stream().map(rating -> {
            //Hotel hotels = restTemplate.getForObject("http://HotelService/hotel/"+rating.getHotelId(), Hotel.class);
            Hotel hotels = hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotels);
            return rating;
        }).collect(Collectors.toList());


        user.setRatings(listRating);
        return user;
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
