package com.os.popularmoviesstage2.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.os.popularmoviesstage2.app.PopularMoviesS2App;
import com.os.popularmoviesstage2.di.DaggerMainActivityComponent;
import com.os.popularmoviesstage2.models.MoviePreview;
import com.os.popularmoviesstage2.viewmodels.PopularMoviesFragmentViewModel;
import com.os.popularmoviesstage2.viewmodels.PopularMoviesFragmentViewModelFactory;

import javax.inject.Inject;

public class PopularMoviesFragment extends MoviesListBaseFragment {
    private static final String TAG = PopularMoviesFragment.class.getSimpleName();

    @Inject
    PopularMoviesFragmentViewModelFactory viewModelFactory;
    private PopularMoviesFragmentViewModel viewModel;

    public PopularMoviesFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerMainActivityComponent.builder().coreComponent(((PopularMoviesS2App) getActivity().getApplication()).getCoreComponent()).build().inject(this);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(PopularMoviesFragmentViewModel.class);

        viewModel.getPopularMovies().observe(this, movies -> {
            adapter.updateData(movies);
            refreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "Refreshing popular movies");
        viewModel.refreshPopularMovies();
    }

    @Override
    public void onMovieClicked(MoviePreview movie) {
        mListener.onMovieSelected(movie);
    }
}
