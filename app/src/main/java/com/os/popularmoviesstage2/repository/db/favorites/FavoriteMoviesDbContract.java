package com.os.popularmoviesstage2.repository.db.favorites;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Omar on 25-Feb-18 9:01 PM
 */

public final class FavoriteMoviesDbContract {
    private FavoriteMoviesDbContract() {}

    public static final String AUTHORITY = "com.os.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favoriteMovies";

    public static class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favoriteMovies";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BACKDROP_URL = "backDropUrl";
    }
}
