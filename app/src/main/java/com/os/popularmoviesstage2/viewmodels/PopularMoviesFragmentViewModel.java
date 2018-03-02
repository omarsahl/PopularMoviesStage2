package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.os.popularmoviesstage2.models.MoviePreview;
import com.os.popularmoviesstage2.repository.MoviesRepository;
import com.os.popularmoviesstage2.utils.LiveDataUtils;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 28-Feb-18 8:48 PM
 */

public class PopularMoviesFragmentViewModel extends ViewModel {
    private static final String TAG = PopularMoviesFragmentViewModel.class.getSimpleName();
    private MoviesRepository moviesRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<MoviePreview>> popularMovies;

    public PopularMoviesFragmentViewModel(MoviesRepository moviesRepository, CompositeDisposable compositeDisposable) {
        this.moviesRepository = moviesRepository;
        this.compositeDisposable = compositeDisposable;
    }

    public void refreshPopularMovies() {
        popularMovies = LiveDataUtils.liveDataFromObservable(popularMovies, moviesRepository.getPopularMovies(), compositeDisposable);
    }

    public LiveData<List<MoviePreview>> getPopularMovies() {
        if (popularMovies == null || popularMovies.getValue() == null) {
            Log.d(TAG, "getMovie: popular movies is null, refreshing livedata");
            refreshPopularMovies();
        }
        return popularMovies;
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "Clearing PopularMoviesFragmentViewModel data");
        compositeDisposable.clear();
    }
}
