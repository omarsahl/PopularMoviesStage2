package com.os.popularmoviesstage2.di.appmodule;

import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.repository.MoviesRepository;
import com.os.popularmoviesstage2.repository.api.MovieDbApi;

import javax.inject.Singleton;

import dagger.Component;
import io.objectbox.BoxStore;

/**
 * Created by Omar on 28-Feb-18 7:13 AM
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, ObjectBoxModule.class})
public interface CoreComponent {
    App getApp();

    MovieDbApi getMovieDbApi();

    BoxStore getBoxStore();

    MoviesRepository getMoviesRepository();
}
