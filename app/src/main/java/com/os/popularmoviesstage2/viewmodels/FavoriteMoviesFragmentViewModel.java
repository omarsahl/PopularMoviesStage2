package com.os.popularmoviesstage2.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;

import com.os.popularmoviesstage2.repository.FavoriteMoviesRepository;
import com.os.popularmoviesstage2.utils.LiveDataUtils;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 05-Mar-18 12:11 AM
 */

public class FavoriteMoviesFragmentViewModel extends ViewModel {
    private static final String TAG = FavoriteMoviesFragmentViewModel.class.getSimpleName();

    private FavoriteMoviesRepository repository;
    private CompositeDisposable disposable;
    private MutableLiveData<Cursor> cursor;

    public FavoriteMoviesFragmentViewModel(FavoriteMoviesRepository repository, CompositeDisposable disposable) {
        this.repository = repository;
        this.disposable = disposable;
    }

    public void refreshFavoritesCursor() {
        cursor = LiveDataUtils.liveDataFromObservable(cursor, repository.queryAllFavoriteMovies(), disposable);

    }

    public LiveData<Cursor> getFavoriteMoviesCursor() {
        refreshFavoritesCursor();
        return cursor;
    }

    @Override
    protected void onCleared() {
        if (cursor != null && cursor.getValue() != null) cursor.getValue().close();
        if (disposable != null) disposable.clear();
    }
}
