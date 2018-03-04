package com.os.popularmoviesstage2.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import com.os.popularmoviesstage2.adapters.ReviewsAdapter;
import com.os.popularmoviesstage2.adapters.VideosAdapter;
import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.databinding.ActivityMovieDetailsBinding;
import com.os.popularmoviesstage2.di.mainacitivtymodule.DaggerMainActivityComponent;
import com.os.popularmoviesstage2.models.Actor;
import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.models.MovieCredits;
import com.os.popularmoviesstage2.models.MovieReviews;
import com.os.popularmoviesstage2.models.MovieVideos;
import com.os.popularmoviesstage2.models.Review;
import com.os.popularmoviesstage2.models.Video;
import com.os.popularmoviesstage2.utils.MovieDetailsUtils;
import com.os.popularmoviesstage2.viewmodels.MovieDetailsActivityViewModel;
import com.os.popularmoviesstage2.viewmodels.MovieDetailsActivityViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import mehdi.sakout.fancybuttons.FancyButton;

public class MovieDetailsActivity extends BaseActivity implements ReviewsAdapter.OnReviewClickListener, VideosAdapter.OnVideoSelectedListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private static final int TOOLBAR_TITLE_VISIBILITY_THRESHOLD = 20; // 20px
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 400;
    private static final int MAX_CAST_SIZE = 10;
    private static final int MAX_REVIEWS_SIZE = 10;

    public static final String MOVIE_EXTRA_KEY = "movieExtra";

    private ActivityMovieDetailsBinding binding;
    private Toolbar toolbar;
    private boolean isToolbarTitleVisible = false;

    @Inject
    MovieDetailsActivityViewModelFactory factory;
    private MovieDetailsActivityViewModel viewModel;

    private ActorsAdapter actorsAdapter;
    private ReviewsAdapter reviewsAdapter;
    private VideosAdapter videosAdapter;

    private String youtubeVideoId;

    private CompositeDisposable disposable;
    private Movie movie;
    private boolean isMovieInFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        setupUi();

        DaggerMainActivityComponent.builder().coreComponent(((App) getApplication()).getCoreComponent()).build().inject(this);
        viewModel = ViewModelProviders.of(this, factory).get(MovieDetailsActivityViewModel.class);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        long id = intent.getLongExtra(MOVIE_EXTRA_KEY, -1L);
        if (id == -1L) {
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

        viewModel.getIsMovieInFavorites().observe(this, isInFavorites -> {
            Log.d(TAG, "onCreate: movie is in favorites? " + isInFavorites);
            isMovieInFavorites = isInFavorites;
            adjustAddToFavoritesButton(isInFavorites);
        });
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
        LinearLayoutManager actorsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        actorsList.setLayoutManager(actorsLayoutManager);
        actorsList.setNestedScrollingEnabled(true);
        actorsList.addItemDecoration(new ActorsListItemDecoration(25));
        actorsList.setHasFixedSize(true);
        actorsList.setAdapter(actorsAdapter);

        reviewsAdapter = new ReviewsAdapter(this, this);
        RecyclerView reviewsList = binding.movieDetailsLayout.reviewsList;
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewsList.setNestedScrollingEnabled(true);
        reviewsList.setLayoutManager(reviewsLayoutManager);
        reviewsList.setHasFixedSize(true);
        reviewsList.setAdapter(reviewsAdapter);

        videosAdapter = new VideosAdapter(this, this);
        RecyclerView videosList = binding.movieDetailsLayout.videosList;
        videosList.setNestedScrollingEnabled(true);
        LinearLayoutManager videosLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        videosList.addItemDecoration(new ActorsListItemDecoration(30));
        videosList.setLayoutManager(videosLayoutManager);
        videosList.setHasFixedSize(true);
        videosList.setAdapter(videosAdapter);

    }

    private void bindUi(Movie movie) {
        MovieVideos movieVideos = movie.getVideos().getTarget();
        List<Video> videos = movieVideos.getResults();
        if (videos == null || videos.size() <= 0) {
            Snackbar.make(getSnackbarParent(), "Couldn't load any videos.", Snackbar.LENGTH_LONG).show();
        } else {
            videosAdapter.updateData(videos);
            youtubeVideoId = videos.get(0).getKey();
        }

        MovieCredits credits = movie.getCredits().getTarget();
        List<Actor> actors = credits.getCast();
        if (actors == null || actors.size() <= 0) {
            Snackbar.make(getSnackbarParent(), "Couldn't load movie cast", Snackbar.LENGTH_LONG);
        } else {
            List<Actor> list = actors.subList(0, Math.min(actors.size(), MAX_CAST_SIZE));
            Collections.sort(list, (o1, o2) -> o1.getOrder() - o2.getOrder());
            actorsAdapter.updateData(list);
        }

        MovieReviews movieReviews = movie.getReviews().getTarget();
        List<Review> reviews = movieReviews.getReviews();
        if (reviews == null || reviews.size() <= 0) {
            Snackbar.make(getSnackbarParent(), "Couldn't load movie reviews", Snackbar.LENGTH_LONG);
        } else {
            List<Review> list = reviews.subList(0, Math.min(reviews.size(), MAX_REVIEWS_SIZE));
            reviewsAdapter.updateData(list);
        }


        binding.setHandlers(this);
        binding.setMovie(movie);
        this.movie = movie;


        disposable.add(Completable.mergeArray(
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
                )
        );
    }

    private void adjustAddToFavoritesButton(Boolean isInFavorites) {
        FancyButton favoritesButton = binding.movieDetailsLayout.addToFavoriteButton;
        if (isInFavorites) {
            favoritesButton.setText(getString(R.string.remove_from_favorites));
            favoritesButton.setIconResource("\uf00d");
        } else {
            favoritesButton.setText(getString(R.string.add_to_favorites));
            favoritesButton.setIconResource("\uf004");
        }
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
    protected View getSnackbarParent() {
        return findViewById(R.id.root);
    }

    @Override
    public void onReviewClicked(Review review) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(review.getUrl()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Snackbar.make(getSnackbarParent(), "No app found to handle this request", Snackbar.LENGTH_SHORT);
        }
    }

    public void onClickAddToFavorites(View v) {
        if (isMovieInFavorites) {
            viewModel.removeMovieFromFavorites(movie);
            Snackbar.make(getSnackbarParent(), "Removed from favorites", Snackbar.LENGTH_LONG).show();
        } else {
            viewModel.addMovieToFavorites(movie);
            Snackbar.make(getSnackbarParent(), "Added to favorites", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVideoSelected(Video video) {
        openTrailerOnYouTube(this, video.getKey());
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) disposable.clear();
        super.onDestroy();
    }
}
