package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesPage {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<MoviePreview> movies;

    public MoviesPage() {
    }

    public int getPage() {
        return page;
    }

    public List<MoviePreview> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "MoviesPage{" +
                "page=" + page +
                ", movies=" + movies +
                '}';
    }
}
