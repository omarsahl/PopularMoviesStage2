package com.os.popularmoviesstage2.utils;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Omar on 27-Feb-18 6:25 PM
 */

public final class LiveDataUtils {
    private static final String TAG = LiveDataUtils.class.getSimpleName();

    public static <T> MutableLiveData<T> liveDataFromObservable(MutableLiveData<T> data, Observable<T> observable, CompositeDisposable disposable) {
        if (data == null) data = new MutableLiveData<>();
        disposable.add(observable.subscribe(data::setValue, (throwable -> Log.e(TAG, "liveDataFromObservable: ", throwable))));
        return data;
    }
}
