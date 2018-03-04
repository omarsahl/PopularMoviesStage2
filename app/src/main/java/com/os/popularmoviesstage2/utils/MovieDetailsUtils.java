package com.os.popularmoviesstage2.utils;

import android.annotation.SuppressLint;

import com.os.popularmoviesstage2.models.Movie;

/**
 * Created by Omar on 23-Feb-18 12:14 AM
 */

public final class MovieDetailsUtils {
    private static final Movie NULL_MOVIE = new Movie(-1L, null, null, null, -1f, null, null, -1, -1, -1L, -1L, -1L);

    private MovieDetailsUtils() {}

    @SuppressLint("DefaultLocale")
    public static String formatMovieDuration(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    public static float mapNumberToRange(float value, float iStart, float iEnd, float oStart, float oEnd) {
        return (value - iStart) * ((oEnd - oStart) / (iEnd - iStart)) + oStart;
    }

    public static boolean isNullMovie(Movie movie) {
        return movie.getId() == -1L && movie.getDuration() == -1 && movie.getVoteCount() == -1;
    }

    public static Movie getNullMovie() {
        return NULL_MOVIE;
    }
}
