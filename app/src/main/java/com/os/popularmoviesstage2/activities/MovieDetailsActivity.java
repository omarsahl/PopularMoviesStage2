package com.os.popularmoviesstage2.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.adapters.ActorsAdapter;
import com.os.popularmoviesstage2.adapters.ActorsListItemDecoration;
import com.os.popularmoviesstage2.app.PopularMoviesS2App;
import com.os.popularmoviesstage2.databinding.ActivityMovieDetailsBinding;
import com.os.popularmoviesstage2.di.DaggerMainActivityComponent;
import com.os.popularmoviesstage2.models.Actor;
import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.models.MovieCredits;
import com.os.popularmoviesstage2.utils.MovieDetailsUtils;
import com.os.popularmoviesstage2.viewmodels.MovieDetailsActivityViewModel;
import com.os.popularmoviesstage2.viewmodels.MovieDetailsActivityViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class MovieDetailsActivity extends BaseActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private static final int TOOLBAR_TITLE_VISIBILITY_THRESHOLD = 20; // 20px
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 400;
    public static final String MOVIE_EXTRA_KEY = "movieExtra";

    private ActivityMovieDetailsBinding binding;
    private Toolbar toolbar;
    private boolean isToolbarTitleVisible = false;

    @Inject
    MovieDetailsActivityViewModelFactory factory;
    private MovieDetailsActivityViewModel viewModel;

    private ActorsAdapter actorsAdapter;

    private String youtubeVideoId;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        setupUi();

        DaggerMainActivityComponent.builder().coreComponent(((PopularMoviesS2App) getApplication()).getCoreComponent()).build().inject(this);
        viewModel = ViewModelProviders.of(this, factory).get(MovieDetailsActivityViewModel.class);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        long id = intent.getLongExtra(MOVIE_EXTRA_KEY, -1L);
        if (id == -1) {
            closeOnError();
            return;
        }

        viewModel.getMovie(id).observe(this, movie -> {
            Log.d(TAG, "onCreate: got this movie: " + movie);
            if (MovieDetailsUtils.isNullMovie(movie)) {
                hideProgressBar();
                showNoMovieErrorDialog();
                return;
            }
            bindUi(movie);
        });
    }

    private void showNoMovieErrorDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.loading_error_dialog_layout, false)
                .dismissListener(dialogInterface -> MovieDetailsActivity.this.finish())
                .build();

        dialog.getCustomView().findViewById(R.id.goBackButton).setOnClickListener(v -> {
            dialog.dismiss();
            MovieDetailsActivity.this.finish();
        });

        dialog.show();
    }

    private void setupUi() {
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        showProgressBar();
        hideDetailsUi();

        binding.collapsingToolbarLayout.setScrimAnimationDuration(DEFAULT_SCRIM_ANIMATION_DURATION);
        startAlphaAnimation(binding.toolbarTitle, 0, View.INVISIBLE);
        binding.appBarRoot.addOnOffsetChangedListener((appBarLayout, negOffset) -> {
            int totalScroll = appBarLayout.getTotalScrollRange();
            int currentScroll = totalScroll + negOffset;
            int toolbarHeight = toolbar.getHeight();

            if (currentScroll <= toolbarHeight + TOOLBAR_TITLE_VISIBILITY_THRESHOLD) {
                if (!isToolbarTitleVisible) {
                    startAlphaAnimation(binding.toolbarTitle, DEFAULT_SCRIM_ANIMATION_DURATION, View.VISIBLE);
                    isToolbarTitleVisible = true;
                }
            } else {
                if (isToolbarTitleVisible) {
                    startAlphaAnimation(binding.toolbarTitle, DEFAULT_SCRIM_ANIMATION_DURATION, View.INVISIBLE);
                    isToolbarTitleVisible = false;
                }
            }
        });

        actorsAdapter = new ActorsAdapter(this);
        RecyclerView actorsList = binding.movieDetailsLayout.actorsList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        actorsList.setLayoutManager(layoutManager);
        actorsList.addItemDecoration(new ActorsListItemDecoration(25));
        actorsList.setHasFixedSize(true);
        actorsList.setAdapter(actorsAdapter);
    }

    private void bindUi(Movie movie) {
//        List<Video> videos = movie.getVideos();
//        if (videos == null || videos.size() <= 0)
//            Snackbar.make(getSnackbarParent(), "Couldn't load any videos.", Snackbar.LENGTH_LONG).show();
//        else
//            youtubeVideoId = videos.get(0).getKey();

        MovieCredits credits = movie.getCredits().getTarget();
        List<Actor> actors = credits.getCast();
        if (actors == null || actors.size() <= 0) {
            Snackbar.make(getSnackbarParent(), "Couldn't load movies cast", Snackbar.LENGTH_LONG);
        } else {
            actorsAdapter.updateData(actors.subList(0, Math.min(actors.size(), 10)));
        }

        binding.setHandlers(this);
        binding.setMovie(movie);


        disposable = Completable.mergeArray(
                Completable.fromAction(() -> Picasso.with(MovieDetailsActivity.this)
                        .load(getString(R.string.movies_db_poster_base_url_backdrop_w780) + movie.getBackDropUrl())
                        .into(binding.movieBackdrop)),

                Completable.fromAction(() -> Picasso.with(this)
                        .load(getString(R.string.movies_db_poster_base_url_poster_w342) + movie.getPosterUrl())
                        .into(binding.movieDetailsLayout.moviePoster)))
                .subscribe(
                        () -> {
                            hideProgressBar();
                            showDetailsUi();
                        },
                        e -> {
                            Log.e(TAG, "bindUi: error loading movie's poster and backdrop image, skipping layout.", e);
                            Snackbar.make(getSnackbarParent(), "Couldn't load movie details, Try again", Snackbar.LENGTH_LONG).show();
                            closeOnError();
                        }
                );
    }

    private void closeOnError() {
        Toast.makeText(this, "Error Loading Movie Details", Toast.LENGTH_LONG).show();
        finish();
    }

    private void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation =
                (visibility == View.VISIBLE) ? new AlphaAnimation(0f, 1f) : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void showDetailsUi() {
        binding.detailsScrollView.setVisibility(View.VISIBLE);
        binding.appBarRoot.setVisibility(View.VISIBLE);
    }

    private void hideDetailsUi() {
        binding.appBarRoot.setVisibility(View.GONE);
        binding.detailsScrollView.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        binding.movieDetailsProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.movieDetailsProgressBar.setVisibility(View.GONE);
    }

    public void onVideoClicked(View view) {
        openTrailerOnYouTube(this, youtubeVideoId);
    }

    public void openTrailerOnYouTube(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        super.onDestroy();
    }

    @Override
    protected View getSnackbarParent() {
        return findViewById(R.id.root);
    }
}
