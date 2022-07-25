package com.microservice.MovieCatalog.service;

import com.microservice.MovieCatalog.model.Movie;
import com.microservice.MovieCatalog.model.Rating;

public interface IMovieInfoService {
	public Movie getMovie(Rating rating);
}
