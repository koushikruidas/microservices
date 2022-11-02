package com.microservice.MovieCatalog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.MovieCatalog.model.Rating;
import com.microservice.MovieCatalog.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class RatingService implements IRatingService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getRatingFallback", 
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // no of previous request hystrix going to look up
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), // break circuit if 50% error
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"), // break circuit for 5 secs
			},
			threadPoolKey = "ratingPool", // this is bulkhead pattern to specify the queue size for this method.
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10")
			}
			)
	public UserRating getRating(int userId) {
		UserRating ratings = 
				restTemplate.getForObject("http://RATING-SERVICE/user/"+userId, UserRating.class);
		return ratings;
	}
	
	public UserRating getRatingFallback(int userId) {
		List<Rating> list = new ArrayList<>();
		list.add(new Rating(0, 0));
		return new UserRating(userId, list);
	}
}
