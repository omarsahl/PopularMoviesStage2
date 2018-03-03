package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;
import com.os.popularmoviesstage2.utils.gson.SkipSerialization;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Omar on 01-Mar-18 8:28 PM
 */

@Entity
public class MovieCredits {
    @SerializedName("id")
    @Id(assignable = true)
    private long id;

    @SerializedName("cast")
    @Backlink
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
