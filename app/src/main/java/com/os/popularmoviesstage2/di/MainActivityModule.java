package com.os.popularmoviesstage2.di;

import com.os.popularmoviesstage2.repository.MoviesRepository;
import com.os.popularmoviesstage2.viewmodels.MovieDetailsActivityViewModelFactory;
import com.os.popularmoviesstage2.viewmodels.PopularMoviesFragmentViewModelFactory;
import com.os.popularmoviesstage2.viewmodels.TopRatedMoviesFragmentViewModelFactory;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 28-Feb-18 8:38 AM
 */

@MainActivityScope
@Module
public class MainActivityModule {
    @Provides
    PopularMoviesFragmentViewModelFactory providePopularMoviesFragmentViewModelFactory(MoviesRepository repository, CompositeDisposable compositeDisposable) {
        return new PopularMoviesFragmentViewModelFactory(repository, compositeDisposable);
    }

    @Provides
    TopRatedMoviesFragmentViewModelFactory provideTopRatedMoviesFragmentViewModelFactory(MoviesRepository repository, CompositeDisposable compositeDisposable) {
        return new TopRatedMoviesFragmentViewModelFactory(repository, compositeDisposable);
    }

    @Provides
    MovieDetailsActivityViewModelFactory provideMovieDetailsActivityViewModelFactory(MoviesRepository repository, CompositeDisposable compositeDisposable) {
        return new MovieDetailsActivityViewModelFactory(repository, compositeDisposable);
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
