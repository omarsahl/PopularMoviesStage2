package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;
import com.os.popularmoviesstage2.utils.gson.SkipSerialization;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Omar on 25-Feb-18 5:59 PM
 */

@Entity
public class Review {
    @Id
    @SkipSerialization
    private long id;

    @SerializedName("id")
    private String strId;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public Review() {}

    public Review(long id, String strId, String author, String content, String url) {
        this.id = id;
        this.strId = strId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getStrId() {
        return strId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
