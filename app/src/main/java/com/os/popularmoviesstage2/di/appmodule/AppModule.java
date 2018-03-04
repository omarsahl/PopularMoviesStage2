package com.os.popularmoviesstage2.di.appmodule;

import android.content.Context;

import com.os.popularmoviesstage2.app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Omar on 28-Feb-18 6:53 AM
 */

@Module
@Singleton
public class AppModule {
    private Context context;
    private App app;

    public AppModule(App app, Context context) {
        this.context = context;
        this.app = app;
    }

    @Provides
    @Singleton
    @ApplicationContext
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public App provideApp() {
        return app;
    }
}
