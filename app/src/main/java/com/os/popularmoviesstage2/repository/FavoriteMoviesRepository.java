package com.os.popularmoviesstage2.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.os.popularmoviesstage2.app.App;
import com.os.popularmoviesstage2.di.mainacitivtymodule.MainActivityScope;
import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_BACKDROP_URL;
import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_ID;
import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_TIMESTAMP;
import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_TITLE;
import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.CONTENT_URI;

/**
 * Created by Omar on 04-Mar-18 5:03 PM
 */

//@Singleton
@MainActivityScope
public class FavoriteMoviesRepository {
    private static final String TAG = FavoriteMoviesRepository.class.getSimpleName();
    private App app;

    @Inject
    public FavoriteMoviesRepository(App app) {
        this.app = app;
    }

    public void addMovieToFavorites(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_ID, movie.getId());
        cv.put(COLUMN_NAME_TITLE, movie.getTitle());
        cv.put(COLUMN_NAME_BACKDROP_URL, movie.getBackDropUrl());

        Completable.fromAction(() -> app.getContentResolver()
                .insert(FavoriteMoviesDbContract.FavoritesEntry.CONTENT_URI, cv))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "addMovieToFavorites: Added to favorites " + movie.getId() + "/" + movie.getTitle()),
                        e -> Log.e(TAG, "addMovieToFavorites: couldn't add " + movie.getId() + "/" + movie.getTitle() + "to favorites db", e)
                );
    }

    public void removeMovieFromFavorites(Movie movie) {
        Uri uri = FavoriteMoviesDbContract.FavoritesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        Completable.fromAction(() -> app.getContentResolver()
                .delete(uri, null, null))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "removeMovieFromFavorites: Removed " + movie.getId() + "/" + movie.getTitle() + "from favorites"),
                        e -> Log.e(TAG, "removeMovieFromFavorites: couldn't remove " + movie.getId() + "/" + movie.getTitle() + "from favorites", e)
                );
    }

    public Observable<Boolean> checkMovieInFavorites(long id) {
        Uri uri = CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        return Observable.fromCallable(() -> app.getContentResolver()
                .query(uri, null, "id=?", new String[]{String.valueOf(id)}, null))
                .subscribeOn(Schedulers.io())
                .map(cursor -> {
                    Boolean isInFavorites = cursor.getCount() > 0;
                    cursor.close();
                    return isInFavorites;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Cursor> queryAllFavoriteMovies() {
        return Observable.fromCallable(() -> app.getContentResolver()
                .query(CONTENT_URI, null, null, null, COLUMN_NAME_TIMESTAMP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
