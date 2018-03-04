package com.os.popularmoviesstage2.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.di.mainacitivtymodule.DaggerMainActivityComponent;
import com.os.popularmoviesstage2.models.MoviePreview;
import com.os.popularmoviesstage2.viewmodels.TopRatedMoviesFragmentViewModel;
import com.os.popularmoviesstage2.viewmodels.TopRatedMoviesFragmentViewModelFactory;

import javax.inject.Inject;

public class TopRatedMoviesFragment extends MoviesListBaseFragment {
    private static final String TAG = TopRatedMoviesFragment.class.getSimpleName();

    @Inject
    TopRatedMoviesFragmentViewModelFactory viewModelFactory;
    private TopRatedMoviesFragmentViewModel viewModel;

    public TopRatedMoviesFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerMainActivityComponent.builder().coreComponent(((App) getActivity().getApplication()).getCoreComponent()).build().inject(this);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(TopRatedMoviesFragmentViewModel.class);

        viewModel.getTopRatedMovies().observe(this, movies -> {
            adapter.updateData(movies);
            refreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "Refreshing top rated movies");
        viewModel.refreshTopRatedMovies();
    }

    @Override
    public void onMovieClicked(MoviePreview movie) {
        mListener.onMovieSelected(movie);
    }
}
