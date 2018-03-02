package com.os.popularmoviesstage2.app;

import android.app.Application;
import android.util.Log;

import com.os.popularmoviesstage2.di.AppModule;
import com.os.popularmoviesstage2.di.CoreComponent;
import com.os.popularmoviesstage2.di.DaggerCoreComponent;

import io.objectbox.BoxStore;

/**
 * Created by Omar on 26-Feb-18 9:44 PM
 */

public class PopularMoviesS2App extends Application {
    private static final String TAG = PopularMoviesS2App.class.getSimpleName();

    private BoxStore boxStore;
    private CoreComponent coreComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        coreComponent = DaggerCoreComponent.builder().appModule(new AppModule(this)).build();
        boxStore = coreComponent.getBoxStore();
        Log.d(TAG, "Using ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public CoreComponent getCoreComponent() {
        return coreComponent;
    }
}
