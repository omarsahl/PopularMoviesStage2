package com.os.popularmoviesstage2.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.popularmoviesstage2.BuildConfig;
import com.os.popularmoviesstage2.app.Constants;
import com.os.popularmoviesstage2.connectivity.NetworkMonitor;
import com.os.popularmoviesstage2.connectivity.NoNetworkException;
import com.os.popularmoviesstage2.repository.api.MovieDbApi;
import com.os.popularmoviesstage2.utils.gson.SkipSerializationStrategy;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Omar on 28-Feb-18 6:54 AM
 */

@Module
public class NetworkModule {
    @Provides
    @Singleton
    public MovieDbApi provideMovieDbApi(Retrofit retrofit) {
        return retrofit.create(MovieDbApi.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory gsonFactory, RxJava2CallAdapterFactory rxJava2CallFactory) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(gsonFactory)
                .addCallAdapterFactory(rxJava2CallFactory)
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context) {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request
                            .url()
                            .newBuilder()
                            .addEncodedQueryParameter(Constants.API_KEY_QUERY_KEY, BuildConfig.API_KEY)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .addInterceptor(chain -> {
                    if (NetworkMonitor.getNetworkStatus(context) == NetworkMonitor.State.CONNECTED) {
                        return chain.proceed(chain.request());
                    } else {
                        throw new NoNetworkException("No Network Connection");
                    }
                })
//                .retryOnConnectionFailure(true)
                .build();
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setExclusionStrategies(new SkipSerializationStrategy())
                .create();
    }

    @Provides
    @Singleton
    public GsonConverterFactory providesGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    public RxJava2CallAdapterFactory providesRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }
}
