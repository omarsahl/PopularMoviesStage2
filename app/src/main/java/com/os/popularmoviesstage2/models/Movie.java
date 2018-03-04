package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;
import com.os.popularmoviesstage2.utils.gson.SkipSerialization;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Movie {
    @Id(assignable = true)
    @SerializedName("id")
    private long id;

    @SerializedName("original_title")
    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterUrl;

    @SerializedName("vote_average")
    private float rating;

    @SerializedName("overview")
    private String overview;

    @SerializedName("backdrop_path")
    private String backDropUrl;

    @SerializedName("runtime")
    private int duration;

    @SerializedName("vote_count")
    private int voteCount;

    @SkipSerialization
    private ToOne<MovieCredits> credits;

    @SkipSerialization
    private ToOne<MovieReviews> reviews;

    @SkipSerialization
    private ToOne<MovieVideos> videos;

    public Movie() {}

    public Movie(long id, String title, String releaseDate, String posterUrl, float rating, String overview, String backDropUrl, int duration, int voteCount, long creditsId, long reviewsId ,long videosId) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.overview = overview;
        this.backDropUrl = backDropUrl;
        this.duration = duration;
        this.voteCount = voteCount;
        this.credits.setTargetId(creditsId);
        this.reviews.setTargetId(reviewsId);
        this.videos.setTargetId(videosId);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public float getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackDropUrl() {
        return backDropUrl;
    }

    public int getDuration() {
        return duration;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setBackDropUrl(String backDropUrl) {
        this.backDropUrl = backDropUrl;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public ToOne<MovieCredits> getCredits() {
        return credits;
    }

    public void setCredits(ToOne<MovieCredits> credits) {
        this.credits = credits;
    }

    public ToOne<MovieReviews> getReviews() {
        return reviews;
    }

    public void setReviews(ToOne<MovieReviews> reviews) {
        this.reviews = reviews;
    }

    public ToOne<MovieVideos> getVideos() {
        return videos;
    }

    public void setVideos(ToOne<MovieVideos> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", rating=" + rating +
                ", overview='" + overview + '\'' +
                ", backDropUrl='" + backDropUrl + '\'' +
                ", duration=" + duration +
                ", voteCount=" + voteCount +
                '}';
    }
}
