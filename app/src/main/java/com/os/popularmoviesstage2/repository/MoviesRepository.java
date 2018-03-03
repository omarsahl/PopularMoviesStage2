package com.os.popularmoviesstage2.repository;

import android.util.Log;

import com.os.popularmoviesstage2.app.Constants;
import com.os.popularmoviesstage2.models.Actor;
import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.models.MovieCredits;
import com.os.popularmoviesstage2.models.MoviePreview;
import com.os.popularmoviesstage2.models.MoviePreview_;
import com.os.popularmoviesstage2.models.Movie_;
import com.os.popularmoviesstage2.models.MoviesPage;
import com.os.popularmoviesstage2.repository.api.MovieDbApi;
import com.os.popularmoviesstage2.utils.MovieDetailsUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Omar on 25-Feb-18 8:41 PM
 */

@Singleton
public class MoviesRepository {
    private static final String TAG = MoviesRepository.class.getSimpleName();

    private MovieDbApi movieApi;
    private BoxStore boxStore;

    @Inject
    public MoviesRepository(MovieDbApi movieApi, BoxStore boxStore) {
        this.movieApi = movieApi;
        this.boxStore = boxStore;
    }

    public Observable<List<MoviePreview>> getPopularMovies() {
        return getPopularMoviesFromApi()
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> {
                    Log.e(TAG, "Couldn't load popular movies from API, Trying DB (" + Thread.currentThread().getName() + ")", e);
                    return getPopularMoviesFromDb();
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<MoviePreview>> getTopRatedMovies() {
        return getTopRatedMoviesFromApi()
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> {
                    Log.e(TAG, "Couldn't load top rated movies from API, Trying DB (" + Thread.currentThread().getName() + ")", e);
                    return getTopRatedMoviesFromDb();
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<MoviePreview>> getPopularMoviesFromApi() {
        return movieApi.getPopularMovies()
                .map(MoviesPage::getMovies)
                .doOnNext(movies -> {
                    Log.d(TAG, "Loaded " + movies.size() + " popular movies from API, Saving it to the DB.");
                    savePopularMoviesToDb(movies);
                });
    }

    private Observable<List<MoviePreview>> getTopRatedMoviesFromApi() {
        return movieApi.getTopRatedMovies()
                .map(MoviesPage::getMovies)
                .doOnNext(movies -> {
                    Log.d(TAG, "Loaded " + movies.size() + " top rated movies from API, Saving it to the DB.");
                    saveTopRatedMoviesToDb(movies);
                });
    }

    private Observable<List<MoviePreview>> getPopularMoviesFromDb() {
        Log.d(TAG, "getPopularMoviesFromDb: getting movies from cache DB. (" + Thread.currentThread().getName() + ")");
        Box<MoviePreview> box = boxStore.boxFor(MoviePreview.class);
        Query query = box.query().equal(MoviePreview_.tag, Constants.TAG_POPULAR).build();
        return Observable.just(query.find());
    }

    private Observable<List<MoviePreview>> getTopRatedMoviesFromDb() {
        Log.d(TAG, "getTopRatedMoviesFromDb: getting movies from cache DB. (" + Thread.currentThread().getName() + ")");
        Box<MoviePreview> box = boxStore.boxFor(MoviePreview.class);
        Query query = box.query().equal(MoviePreview_.tag, Constants.TAG_TOP_RATED).build();
        return Observable.just(query.find());
    }

    private void savePopularMoviesToDb(List<MoviePreview> movies) {
        Completable.fromAction(() -> {
            Box<MoviePreview> box = boxStore.boxFor(MoviePreview.class);
            for (MoviePreview movie : movies) {
                movie.setTag(Constants.TAG_POPULAR);
                box.put(movie);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(throwable -> Log.e(TAG, "savePopularMoviesToDb: error saving popular movies to Db", throwable))
                .subscribe(() -> Log.d(TAG, "Saved " + movies.size() + " Popular movies to ObjectBox DB"));
    }

    private void saveTopRatedMoviesToDb(List<MoviePreview> movies) {
        Completable.fromAction(() -> {
            Box<MoviePreview> box = boxStore.boxFor(MoviePreview.class);
            for (MoviePreview movie : movies) {
                movie.setTag(Constants.TAG_TOP_RATED);
                box.put(movie);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(throwable -> Log.e(TAG, "saveTopRatedMoviesToDb: error saving top rated movies to Db", throwable))
                .subscribe(() -> Log.d(TAG, "Saved " + movies.size() + " Top Rated movies to ObjectBox DB"));
    }

    public Observable<Movie> getMovieDetails(long id) {
        return getMovieDetailsFromApi(id)
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> {
                    Log.e(TAG, "Couldn't load movie (id=" + id + ") from API, Trying DB (" + Thread.currentThread().getName() + ")", e);
                    return getMovieDetailsFromDb(id);
                })
                .onErrorReturnItem(MovieDetailsUtils.getNullMovie())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Movie> getMovieDetailsFromApi(long id) {
        return Observable.zip(
                movieApi.getMovie(id),
                movieApi.getMovieVideos(id),
                movieApi.getMovieCredits(id).doOnNext(this::saveCreditsToDb),
                movieApi.getMovieReviews(id),
                (movie, movieVideos, movieCredits, movieReviews) -> {
//                    // workaround for this exception "io.objectbox.exception.DbDetachedException: Cannot resolve relation for detached entities"
//                    // just for now.
//                    // -------------------
//                    Box<Movie> movieBox = boxStore.boxFor(Movie.class);
//                    movieBox.attach(movie);
//                    // -------------------

                    movie.getCredits().setTarget(movieCredits);
//                        movie.getVideos().addAll(movieVideos.getResults());
//                        movie.getReviews().addAll(movieReviews.getReviews());

                    return movie;
                }).doOnNext(this::saveMovieToDb);
    }

    private Observable<Movie> getMovieDetailsFromDb(long id) {
        Log.d(TAG, "getMovieDetailsFromDb: getting movie from cache DB. (" + Thread.currentThread().getName() + ")");
        Box<Movie> box = boxStore.boxFor(Movie.class);
        return Observable.just(box.query().equal(Movie_.id, id).build().findFirst())
                .doOnNext(movie -> Log.d(TAG, "getMovieDetailsFromDb: found a matching movie in DB? " + (movie != null)))
                .doOnError((e) -> Log.e(TAG, "getMovieDetailsFromDb: Couldn't get movie details from DB", e));
    }

    private void saveMovieToDb(Movie movie) {
        Completable.fromAction(() -> boxStore.boxFor(Movie.class).put(movie))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(throwable -> Log.e(TAG, "saveMovieToDb: Couldn't save movie to ObjectBox DB", throwable))
                .subscribe(() -> Log.d(TAG, "Saved (" + movie.toString() + ")" + " to ObjectBox DB"));
    }

    private void saveCreditsToDb(MovieCredits credits) {
        Completable.fromAction(() -> {
            saveFirst10ActorsInDb(credits);
            boxStore.boxFor(MovieCredits.class).put(credits);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError((e) -> Log.e(TAG, "saveCreditsToDb: Couldn't save credits and cast to DB", e))
                .subscribe(() -> Log.d(TAG, "saveCreditsToDb: Saved " + credits.getId() + " to DB"));
    }

    private void saveFirst10ActorsInDb(MovieCredits credits) {
        List<Actor> cast = credits.getCast();
        int size = cast.size();
        int toIndex = Math.min(size, 10);
        List<Actor> top10Actors = cast.subList(0, toIndex);
        Box<Actor> actorsBox = boxStore.boxFor(Actor.class);
        for (Actor actor : top10Actors) {
            actor.getCredits().setTarget(credits);
        }
        actorsBox.put(top10Actors);
    }
}
