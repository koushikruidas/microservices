package com.microservice.MovieCatalog.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.MovieCatalog.model.CatalogItem;
import com.microservice.MovieCatalog.model.Movie;
import com.microservice.MovieCatalog.model.UserRating;
import com.microservice.MovieCatalog.service.MovieInfoService;
import com.microservice.MovieCatalog.service.RatingService;

@RestController
public class CatalogController {
	
	@Autowired
	private RatingService ratingService;
	@Autowired
	private MovieInfoService movieInfoService;
	
	@GetMapping("/catalog/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") int userId){
		UserRating newRatings = ratingService.getRating(userId);
//		List<Rating> ratings = Arrays.asList(
//				new Rating(1,10),
//				new Rating(2,9),
//				new Rating(3,5)
//				);
		return (newRatings.getRatings().stream()
		.map(rating -> {
			Movie movie = movieInfoService.getMovie(rating);
			return new CatalogItem(movie.getName(), "A place that is very quite", rating.getRating());	
		})
		.collect(Collectors.toList())
		);
	}
} 
