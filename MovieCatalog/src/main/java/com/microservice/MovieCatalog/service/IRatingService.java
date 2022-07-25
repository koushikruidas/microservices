package com.microservice.MovieCatalog.service;

import com.microservice.MovieCatalog.model.UserRating;

public interface IRatingService {
	public UserRating getRating(int userId);
}
