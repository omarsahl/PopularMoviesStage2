package com.os.popularmoviesstage2.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.adapters.FavoriteMoviesAdapter;
import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.di.mainacitivtymodule.DaggerMainActivityComponent;
import com.os.popularmoviesstage2.models.MoviePreview;
import com.os.popularmoviesstage2.viewmodels.FavoriteMoviesFragmentViewModel;
import com.os.popularmoviesstage2.viewmodels.FavoriteMoviesFragmentViewModelFactory;

import javax.inject.Inject;

public class FavoriteMoviesFragment extends Fragment implements FavoriteMoviesAdapter.OnFavoriteMovieClickedListener, SwipeRefreshLayout.OnRefreshListener {

    private OnFavoriteMoviesFragmentInteractionListener mListener;
    private RecyclerView favoritesList;
    private FavoriteMoviesAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Inject
    FavoriteMoviesFragmentViewModelFactory factory;
    private FavoriteMoviesFragmentViewModel viewModel;
    private SwipeRefreshLayout refreshLayout;

    public FavoriteMoviesFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        favoritesList = root.findViewById(R.id.favoriteMoviesList);
        refreshLayout = root.findViewById(R.id.favoritesRefreshLayout);
        refreshLayout.setOnRefreshListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerMainActivityComponent.builder().coreComponent(((App) getActivity().getApplication()).getCoreComponent()).build().inject(this);

        adapter = new FavoriteMoviesAdapter(this, getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        favoritesList.setLayoutManager(layoutManager);
        favoritesList.setAdapter(adapter);
        refreshLayout.setRefreshing(true);
        viewModel = ViewModelProviders.of(getActivity(), factory).get(FavoriteMoviesFragmentViewModel.class);
        viewModel.getFavoriteMoviesCursor().observe(this, cursor -> {
            adapter.updateCursor(cursor);
            refreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteMoviesFragmentInteractionListener) {
            mListener = (OnFavoriteMoviesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFavoriteMoviesFragmentInteractionListener");
        }
    }

    @Override
    public void onRefresh() {
        viewModel.refreshFavoritesCursor();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFavoriteMovieClicked(MoviePreview movie) {
        mListener.onFavoriteMovieClicked(movie);
    }

    public interface OnFavoriteMoviesFragmentInteractionListener {
        void onFavoriteMovieClicked(MoviePreview movie);
    }
}
