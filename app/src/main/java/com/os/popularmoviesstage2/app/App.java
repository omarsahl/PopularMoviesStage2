package com.os.popularmoviesstage2.app;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.os.popularmoviesstage2.di.appmodule.AppModule;
import com.os.popularmoviesstage2.di.appmodule.CoreComponent;
import com.os.popularmoviesstage2.di.appmodule.DaggerCoreComponent;

import io.objectbox.BoxStore;

/**
 * Created by Omar on 26-Feb-18 9:44 PM
 */

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    private BoxStore boxStore;
    private CoreComponent coreComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        coreComponent = DaggerCoreComponent.builder().appModule(new AppModule(this, this)).build();
        boxStore = coreComponent.getBoxStore();
        Log.d(TAG, "Using ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");
        Stetho.initializeWithDefaults(this);
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public CoreComponent getCoreComponent() {
        return coreComponent;
    }
}
