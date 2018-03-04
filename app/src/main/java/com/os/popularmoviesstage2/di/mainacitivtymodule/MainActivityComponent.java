package com.os.popularmoviesstage2.di.mainacitivtymodule;

import com.os.popularmoviesstage2.activities.MovieDetailsActivity;
import com.os.popularmoviesstage2.di.appmodule.CoreComponent;
import com.os.popularmoviesstage2.fragments.FavoriteMoviesFragment;
import com.os.popularmoviesstage2.fragments.PopularMoviesFragment;
import com.os.popularmoviesstage2.fragments.TopRatedMoviesFragment;

import dagger.Component;

/**
 * Created by Omar on 28-Feb-18 8:38 AM
 */

@MainActivityScope
@Component(dependencies = {CoreComponent.class}, modules = {MainActivityModule.class})
public interface MainActivityComponent {
    void inject(PopularMoviesFragment fragment);

    void inject(TopRatedMoviesFragment fragment);

    void inject(FavoriteMoviesFragment fragment);

    void inject(MovieDetailsActivity activity);

}
