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
 * Created by Omar on 28-Feb-18 9:51 PM
 */

public class TopRatedMoviesFragmentViewModel extends ViewModel {
    private static final String TAG = TopRatedMoviesFragmentViewModel.class.getSimpleName();

    private MoviesRepository moviesRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<MoviePreview>> topRatedMovies;

    public TopRatedMoviesFragmentViewModel(MoviesRepository moviesRepository, CompositeDisposable compositeDisposable) {
        this.moviesRepository = moviesRepository;
        this.compositeDisposable = compositeDisposable;
    }

    public void refreshTopRatedMovies() {
        topRatedMovies = LiveDataUtils.liveDataFromObservable(topRatedMovies, moviesRepository.getTopRatedMovies(), compositeDisposable);
    }

    public LiveData<List<MoviePreview>> getTopRatedMovies() {
        if (topRatedMovies == null || topRatedMovies.getValue() == null) refreshTopRatedMovies();
        return topRatedMovies;
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "Clearing TopRatedMoviesFragmentViewModel data");
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
