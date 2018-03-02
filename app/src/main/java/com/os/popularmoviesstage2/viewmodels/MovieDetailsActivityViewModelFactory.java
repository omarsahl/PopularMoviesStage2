package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.os.popularmoviesstage2.repository.MoviesRepository;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 02-Mar-18 12:19 AM
 */

public class MovieDetailsActivityViewModelFactory implements ViewModelProvider.Factory {
    private final MoviesRepository repository;
    private final CompositeDisposable compositeDisposable;

    public MovieDetailsActivityViewModelFactory(MoviesRepository repository, CompositeDisposable compositeDisposable) {
        this.repository = repository;
        this.compositeDisposable = compositeDisposable;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsActivityViewModel(repository, compositeDisposable);
    }
}
