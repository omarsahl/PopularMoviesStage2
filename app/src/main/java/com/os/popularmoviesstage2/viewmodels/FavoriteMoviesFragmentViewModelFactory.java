package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.os.popularmoviesstage2.repository.FavoriteMoviesRepository;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 05-Mar-18 12:22 AM
 */

public class FavoriteMoviesFragmentViewModelFactory implements ViewModelProvider.Factory {
    private FavoriteMoviesRepository repository;
    private CompositeDisposable disposable;

    public FavoriteMoviesFragmentViewModelFactory(FavoriteMoviesRepository repository, CompositeDisposable disposable) {
        this.repository = repository;
        this.disposable = disposable;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoriteMoviesFragmentViewModel(repository, disposable);
    }
}
