package com.os.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.adapters.MoviesAdapter;
import com.os.popularmoviesstage2.models.MoviePreview;

public abstract class MoviesListBaseFragment extends Fragment implements MoviesAdapter.OnMovieClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MoviesListBaseFragment.class.getSimpleName();
    protected static final String CURRENT_LIST_POS_KEY = "currentListPos";
    protected OnMoviesSelectedListener mListener;
    protected RecyclerView moviesRecyclerView;
    protected MoviesAdapter adapter;
    protected GridLayoutManager layoutManager;
    protected SwipeRefreshLayout refreshLayout;

    public MoviesListBaseFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_list_base, container, false);

        boolean isInPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int spanCount = isInPortrait ? 2 : 4;

        moviesRecyclerView = root.findViewById(R.id.moviesRecyclerView);
        adapter = new MoviesAdapter(getActivity(), this);
        layoutManager = new GridLayoutManager(getActivity(), spanCount, GridLayoutManager.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(adapter);

        refreshLayout = root.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoviesSelectedListener) {
            mListener = (OnMoviesSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMoviesSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMoviesSelectedListener {
        void onMovieSelected(MoviePreview movie);
    }
}
