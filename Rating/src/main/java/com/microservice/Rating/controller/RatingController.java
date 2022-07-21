package com.microservice.Rating.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.Rating.model.Rating;
import com.microservice.Rating.model.UserRating;

@RestController
public class RatingController {
	@GetMapping("/getRatings/{movieId}")
	public Rating getRatings(@PathVariable("movieId") int movieId) {
		return new Rating(movieId, 10);
	}
	
	@RequestMapping("/user/{userId}")
    public UserRating getUserRatings(@PathVariable("userId") String userId) {
        UserRating userRating = new UserRating();
        userRating.initData(userId);
        return userRating;

    }
}
