package com.os.popularmoviesstage2.utils;

import android.annotation.SuppressLint;

/**
 * Created by Omar on 23-Feb-18 12:14 AM
 */

public final class MovieDetailsUtils {
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
}
