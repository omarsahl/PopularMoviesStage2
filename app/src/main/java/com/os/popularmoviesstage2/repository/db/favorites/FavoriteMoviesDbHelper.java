package com.os.popularmoviesstage2.repository.db.favorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Omar on 25-Feb-18 11:05 PM
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favorite_movies.db";

    private static final String SQL_CREATE_TABLE_FAVORITES =
            "CREATE TABLE IF NOT EXISTS " + FavoriteMoviesDbContract.FavoritesEntry.TABLE_NAME + " (" +
                    FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE," +
                    FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
                    FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_BACKDROP_URL + " TEXT NOT NULL, " +
                    FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesDbContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
