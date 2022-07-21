package com.microservice.MovieInfo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.MovieInfo.model.Movie;

@RestController
public class MovieInfoController {
	@GetMapping("/getMovie/{movieId}")
	public Movie getMovieInfo(@PathVariable("movieId") int movieId) {
		return new Movie(movieId, "Quite Place");
	}
}
