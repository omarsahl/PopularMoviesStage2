package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.os.popularmoviesstage2.repository.MoviesRepository;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 28-Feb-18 9:52 PM
 */

public class PopularMoviesFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final MoviesRepository repository;
    private final CompositeDisposable compositeDisposable;

    public PopularMoviesFragmentViewModelFactory(MoviesRepository repository, CompositeDisposable compositeDisposable) {
        this.repository = repository;
        this.compositeDisposable = compositeDisposable;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PopularMoviesFragmentViewModel(repository, compositeDisposable);
    }
}
