package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Omar on 25-Feb-18 5:55 PM
 */

public class MovieReviews {
    @SerializedName("id")
    private long id;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Review> reviews;

    @SerializedName("total_pages")
    private int pagesCount;

    @SerializedName("total_results")
    private int reviewsCount;

    public MovieReviews() {}

    public long getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }
}
