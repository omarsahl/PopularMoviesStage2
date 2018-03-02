package com.os.popularmoviesstage2.di;

import android.content.Context;
import android.util.Log;

import com.os.popularmoviesstage2.BuildConfig;
import com.os.popularmoviesstage2.app.PopularMoviesS2App;
import com.os.popularmoviesstage2.models.MyObjectBox;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by Omar on 28-Feb-18 7:27 AM
 */

@Module
public class ObjectBoxModule {
    @Provides
    @Singleton
    public BoxStore provideBoxStore(@ApplicationContext Context context) {
        BoxStore boxStore = MyObjectBox
                .builder()
                .name("movies")
                .androidContext(context)
                .build();

        if (BuildConfig.DEBUG) {
            boolean started = new AndroidObjectBrowser(boxStore).start(context);
            Log.i("ObjectBrowser", "Started: " + started);
        }

        return boxStore;
    }
}
