package com.os.popularmoviesstage2.di.mainacitivtymodule;

import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.repository.FavoriteMoviesRepository;
import com.os.popularmoviesstage2.repository.MoviesRepository;
import com.os.popularmoviesstage2.viewmodels.FavoriteMoviesFragmentViewModelFactory;
import com.os.popularmoviesstage2.viewmodels.MovieDetailsActivityViewModelFactory;
import com.os.popularmoviesstage2.viewmodels.PopularMoviesFragmentViewModelFactory;
import com.os.popularmoviesstage2.viewmodels.TopRatedMoviesFragmentViewModelFactory;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 28-Feb-18 8:38 AM
 */

@Module
public class MainActivityModule {
    @Provides
    @MainActivityScope
    PopularMoviesFragmentViewModelFactory providePopularMoviesFragmentViewModelFactory(MoviesRepository repository, CompositeDisposable compositeDisposable) {
        return new PopularMoviesFragmentViewModelFactory(repository, compositeDisposable);
    }

    @Provides
    @MainActivityScope
    TopRatedMoviesFragmentViewModelFactory provideTopRatedMoviesFragmentViewModelFactory(MoviesRepository repository, CompositeDisposable compositeDisposable) {
        return new TopRatedMoviesFragmentViewModelFactory(repository, compositeDisposable);
    }

    @Provides
    @MainActivityScope
    MovieDetailsActivityViewModelFactory provideMovieDetailsActivityViewModelFactory(App app, MoviesRepository repository, FavoriteMoviesRepository favoriteMoviesRepository, CompositeDisposable compositeDisposable) {
        return new MovieDetailsActivityViewModelFactory(app, repository, favoriteMoviesRepository, compositeDisposable);
    }

    @Provides
    @MainActivityScope
    FavoriteMoviesFragmentViewModelFactory provideFavoriteMoviesFragmentViewModelFactory(FavoriteMoviesRepository favoriteMoviesRepository, CompositeDisposable compositeDisposable) {
        return new FavoriteMoviesFragmentViewModelFactory(favoriteMoviesRepository, compositeDisposable);
    }

    @Provides
    @MainActivityScope
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
