package com.aditya.flixster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amodi on 3/9/17.
 */

public class MovieCollection {

    @SerializedName("results")
    public List<Movie> movieList;

    public MovieCollection() {
        movieList = new ArrayList<>();
    }
}
