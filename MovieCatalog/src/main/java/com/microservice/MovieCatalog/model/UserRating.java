package com.microservice.MovieCatalog.model;

import java.util.List;

public class UserRating {
	private int userId;
	private List<Rating> ratings;
	
	public UserRating() {}

	public UserRating(int userId, List<Rating> ratings) {
		this.userId = userId;
		this.ratings = ratings;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
}
