package com.os.popularmoviesstage2.repository.api;

import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.models.MovieCredits;
import com.os.popularmoviesstage2.models.MovieVideos;
import com.os.popularmoviesstage2.models.MoviesPage;
import com.os.popularmoviesstage2.models.ReviewsContainer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieDbApi {
    @GET("movie/popular")
    Observable<MoviesPage> getPopularMovies();

    @GET("movie/top_rated")
    Observable<MoviesPage> getTopRatedMovies();

    @GET("movie/{id}")
    Observable<Movie> getMovie(@Path("id") long id);

    @GET("movie/{id}/videos")
    Observable<MovieVideos> getMovieVideos(@Path("id") long id);

    @GET("movie/{id}/reviews")
    Observable<ReviewsContainer> getMovieReviews(@Path("id") long id);

    @GET("movie/{id}/credits")
    Observable<MovieCredits> getMovieCredits(@Path("id") long id);
}
