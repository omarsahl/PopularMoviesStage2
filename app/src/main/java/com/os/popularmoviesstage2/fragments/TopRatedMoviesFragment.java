package com.os.popularmoviesstage2.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.app.PopularMoviesS2App;
import com.os.popularmoviesstage2.di.DaggerMainActivityComponent;
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
        DaggerMainActivityComponent.builder().coreComponent(((PopularMoviesS2App) getActivity().getApplication()).getCoreComponent()).build().inject(this);
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
