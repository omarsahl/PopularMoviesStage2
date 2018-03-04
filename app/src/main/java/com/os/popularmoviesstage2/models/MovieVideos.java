package com.os.popularmoviesstage2.models;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Omar on 22-Feb-18 9:53 PM
 */

@Entity
public class MovieVideos {
    @Id(assignable = true)
    private long id;

    @Backlink
    private List<Video> results;

    public MovieVideos() {}

    public MovieVideos(long id, List<Video> results) {
        this.id = id;
        this.results = results;
    }

    public long getId() {
        return id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
