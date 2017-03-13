package com.aditya.flixster.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amodi on 3/9/17.
 */

public class Movie implements Serializable {

    private static final String IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w342";
    private static final String BACKDROP_SIZE = "w1280";
    public long id;
    public List<Long> genreIds;
    public String posterPath;
    public String overview;
    public String originalTitle;
    public String title;
    public String backdropPath;
    public double popularity;
    public long voteCount;
    public double voteAverage;
    public String releaseDate;

    public String getPosterImageUrl() {
        return IMAGE_URL_PREFIX + POSTER_SIZE + posterPath;
    }

    public String getBackdropImageUrl() {
        return IMAGE_URL_PREFIX + BACKDROP_SIZE + backdropPath;
    }

}
