package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Omar on 25-Feb-18 5:55 PM
 */

@Entity
public class MovieReviews {
    @Id(assignable = true)
    @SerializedName("id")
    private long id;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    @Backlink
    private List<Review> reviews;

    @SerializedName("total_pages")
    private int pagesCount;

    @SerializedName("total_results")
    private int reviewsCount;

    public MovieReviews() {}

    public MovieReviews(long id, int page, List<Review> reviews, int pagesCount, int reviewsCount) {
        this.id = id;
        this.page = page;
        this.reviews = reviews;
        this.pagesCount = pagesCount;
        this.reviewsCount = reviewsCount;
    }

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

    public void setId(long id) {
        this.id = id;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
}
