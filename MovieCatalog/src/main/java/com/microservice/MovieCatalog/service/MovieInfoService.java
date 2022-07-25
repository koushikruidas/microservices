package com.microservice.MovieCatalog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.MovieCatalog.model.Movie;
import com.microservice.MovieCatalog.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MovieInfoService implements IMovieInfoService {
	@Autowired
	private RestTemplate restTemplate;
	@HystrixCommand(fallbackMethod = "getMovieFallback",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // no of previous request hystrix going to look up
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), // break circuit if 50% error
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") // break circuit for 5 secs
			}
			)
	public Movie getMovie(Rating rating) {
		Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/getMovie/"+rating.getMovieId(), Movie.class);
		return movie;
	}
	
	public Movie getMovieFallback(Rating rating) {
		return new Movie(0, "Movie not found");
	}
}
