package com.os.popularmoviesstage2.models;

import java.util.List;

/**
 * Created by Omar on 22-Feb-18 9:53 PM
 */

public class MovieVideos {
    private long id;
    private List<Video> results;

    public MovieVideos() {}

    public long getId() {
        return id;
    }

    public List<Video> getResults() {
        return results;
    }
}
