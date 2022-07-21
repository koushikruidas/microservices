package com.microservice.MovieCatalog.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservice.MovieCatalog.model.CatalogItem;
import com.microservice.MovieCatalog.model.Movie;
import com.microservice.MovieCatalog.model.Rating;
import com.microservice.MovieCatalog.model.UserRating;

@RestController
public class CatalogController {
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/catalog/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") int userId){
		UserRating newRatings = restTemplate.getForObject("http://localhost:8082/user/"+userId, UserRating.class);
//		List<Rating> ratings = Arrays.asList(
//				new Rating(1,10),
//				new Rating(2,9),
//				new Rating(3,5)
//				);
		return (newRatings.getRatings().stream()
		.map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8081/getMovie/"+rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(), "A place that is very quite", rating.getRating());	
		})
		.collect(Collectors.toList())
		);
	}
} 
