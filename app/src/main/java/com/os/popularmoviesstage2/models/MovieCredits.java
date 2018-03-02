package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Omar on 01-Mar-18 8:28 PM
 */

public class MovieCredits {
    @SerializedName("id")
    private long id;

    @SerializedName("cast")
    private List<Actor> cast;

    public MovieCredits() {}

    public MovieCredits(long id, List<Actor> cast) {
        this.id = id;
        this.cast = cast;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }
}
