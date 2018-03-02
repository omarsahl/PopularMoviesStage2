package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.repository.MoviesRepository;
import com.os.popularmoviesstage2.utils.LiveDataUtils;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 02-Mar-18 12:13 AM
 */

public class MovieDetailsActivityViewModel extends ViewModel {
    private static final String TAG = MovieDetailsActivityViewModel.class.getSimpleName();
    private MoviesRepository moviesRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<Movie> movie;

    public MovieDetailsActivityViewModel(MoviesRepository moviesRepository, CompositeDisposable compositeDisposable) {
        this.moviesRepository = moviesRepository;
        this.compositeDisposable = compositeDisposable;
    }

    public void refreshPopularMovies(long id) {
        movie = LiveDataUtils.liveDataFromObservable(movie, moviesRepository.getMovieDetails(id), compositeDisposable);
    }

    public LiveData<Movie> getMovie(long id) {
        if (movie == null || movie.getValue() == null) {
            Log.d(TAG, "getMovie: movie is null, refreshing liveData");
            refreshPopularMovies(id);
        }
        return movie;
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "Clearing MovieDetailsActivityViewModel data");
        compositeDisposable.clear();
    }
}
