package com.microservice.user.main.external.service;

import com.microservice.user.main.entity.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "RatingService")
public interface RatingService {

    //Create Rating
    @PostMapping("/rating/")
    ResponseEntity<Rating> createRating(Rating rating);

    //Get Rating
    @GetMapping("/rating/{ratingId}")
    ResponseEntity<Rating> getRating(@PathVariable String ratingId);

}
