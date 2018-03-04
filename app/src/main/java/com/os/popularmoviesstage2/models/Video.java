package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;
import com.os.popularmoviesstage2.utils.gson.SkipSerialization;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Omar on 22-Feb-18 9:53 PM
 */

@Entity
public class Video {
    @Id
    @SkipSerialization
    private long id;

    @SerializedName("id")
    private String strId;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    @SkipSerialization
    private ToOne<MovieVideos> movieVideos;

    public Video() {}

    public Video(long id, String strId, String key, String name, String site, int size, String type, long movieVideosId) {
        this.id = id;
        this.strId = strId;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.movieVideos.setTargetId(movieVideosId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ToOne<MovieVideos> getMovieVideos() {
        return movieVideos;
    }

    public void setMovieVideos(ToOne<MovieVideos> movieVideos) {
        this.movieVideos = movieVideos;
    }
}
