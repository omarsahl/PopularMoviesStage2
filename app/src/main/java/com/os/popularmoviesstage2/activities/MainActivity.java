package com.os.popularmoviesstage2.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.fragments.FavoriteMoviesFragment;
import com.os.popularmoviesstage2.fragments.MoviesListBaseFragment;
import com.os.popularmoviesstage2.fragments.PopularMoviesFragment;
import com.os.popularmoviesstage2.fragments.TopRatedMoviesFragment;
import com.os.popularmoviesstage2.models.MoviePreview;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MoviesListBaseFragment.OnMoviesSelectedListener, FavoriteMoviesFragment.OnFavoriteMoviesFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG_POPULAR = PopularMoviesFragment.class.getSimpleName();
    private static final String FRAGMENT_TAG_TOP_RATED = TopRatedMoviesFragment.class.getSimpleName();
    private static final String FRAGMENT_TAG_FAVORITES = FavoriteMoviesFragment.class.getSimpleName();

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
    }

    private void setupUi() {
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));

        Typeface lato = ResourcesCompat.getFont(this, R.font.lato_regular);
        toolbarTitle.setTypeface(lato);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.setSelectedItemId(R.id.action_popular);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.action_popular:
                PopularMoviesFragment fragmentPopular = (PopularMoviesFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_POPULAR);
                if (fragmentPopular == null) fragmentPopular = new PopularMoviesFragment();
                transaction.replace(R.id.fragmentContainer, fragmentPopular, FRAGMENT_TAG_POPULAR);
                break;
            case R.id.action_top_rated:
                TopRatedMoviesFragment fragmentTopRated = (TopRatedMoviesFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_TOP_RATED);
                if (fragmentTopRated == null) fragmentTopRated = new TopRatedMoviesFragment();
                transaction.replace(R.id.fragmentContainer, fragmentTopRated, FRAGMENT_TAG_TOP_RATED);
                break;
            case R.id.action_favorites:
                FavoriteMoviesFragment fragmentFavorites = (FavoriteMoviesFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_FAVORITES);
                if (fragmentFavorites == null) fragmentFavorites = new FavoriteMoviesFragment();
                transaction.replace(R.id.fragmentContainer, fragmentFavorites, FRAGMENT_TAG_FAVORITES);
                break;
        }
        transaction.commit();
        return true;
    }

    @Override
    protected View getSnackbarParent() {
        return findViewById(R.id.moviesRecyclerViewWrapper);
    }

    @Override
    public void onMovieSelected(MoviePreview movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_EXTRA_KEY, movie.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteMovieClicked(MoviePreview movie) {
        onMovieSelected(movie);
    }
}
