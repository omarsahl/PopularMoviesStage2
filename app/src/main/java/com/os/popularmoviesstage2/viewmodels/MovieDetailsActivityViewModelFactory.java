package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.repository.MoviesRepository;
import com.os.popularmoviesstage2.repository.FavoriteMoviesRepository;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 02-Mar-18 12:19 AM
 */

public class MovieDetailsActivityViewModelFactory implements ViewModelProvider.Factory {
    private final MoviesRepository repository;
    private final FavoriteMoviesRepository favoriteMoviesRepository;
    private final CompositeDisposable compositeDisposable;
    private final App app;


    public MovieDetailsActivityViewModelFactory(App app, MoviesRepository repository, FavoriteMoviesRepository favoriteMoviesRepository, CompositeDisposable compositeDisposable) {
        this.repository = repository;
        this.favoriteMoviesRepository = favoriteMoviesRepository;
        this.compositeDisposable = compositeDisposable;
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsActivityViewModel(app, repository,favoriteMoviesRepository, compositeDisposable);
    }
}
