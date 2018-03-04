package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.repository.MoviesRepository;
import com.os.popularmoviesstage2.repository.FavoriteMoviesRepository;
import com.os.popularmoviesstage2.utils.LiveDataUtils;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 02-Mar-18 12:13 AM
 */

public class MovieDetailsActivityViewModel extends AndroidViewModel {
    private static final String TAG = MovieDetailsActivityViewModel.class.getSimpleName();
    private MoviesRepository moviesRepository;
    private FavoriteMoviesRepository favoriteMoviesRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<Movie> movie;
    private MutableLiveData<Boolean> isMovieInFavorites;

    public MovieDetailsActivityViewModel(App application, MoviesRepository moviesRepository, FavoriteMoviesRepository favoriteMoviesRepository, CompositeDisposable compositeDisposable) {
        super(application);
        this.moviesRepository = moviesRepository;
        this.favoriteMoviesRepository = favoriteMoviesRepository;
        this.compositeDisposable = compositeDisposable;
    }

    public void refreshMovie(long id) {
        movie = LiveDataUtils.liveDataFromObservable(movie, moviesRepository.getMovieDetails(id), compositeDisposable);
        isMovieInFavorites = new MutableLiveData<>();
        checkMovieInFavorites(id);
    }

    public LiveData<Movie> getMovie(long id) {
        if (movie == null || movie.getValue() == null) {
            Log.d(TAG, "getMovie: movie is null, refreshing liveData");
            refreshMovie(id);
        }
        return movie;
    }

    public MutableLiveData<Boolean> getIsMovieInFavorites() {
        return isMovieInFavorites;
    }

    public void checkMovieInFavorites(long id) {
        compositeDisposable.add(
                favoriteMoviesRepository.checkMovieInFavorites(id)
                        .subscribe(bool -> isMovieInFavorites.setValue(bool))
        );
    }

    public void addMovieToFavorites(Movie movie) {
        favoriteMoviesRepository.addMovieToFavorites(movie);
        checkMovieInFavorites(movie.getId());
    }

    public void removeMovieFromFavorites(Movie movie) {
        favoriteMoviesRepository.removeMovieFromFavorites(movie);
        checkMovieInFavorites(movie.getId());
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "Clearing MovieDetailsActivityViewModel data");
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
