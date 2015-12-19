package com.blogjihad.nano.p1.moviedb.data.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alammr on 11/13/15.
 */
public class FavoriteMovieDBHelper  extends SQLiteOpenHelper {


    public static final String LOG_TAG = FavoriteMovieDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "favoriteMovies.db";
    private static final int DATABASE_VERSION = 0;

    public FavoriteMovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES + "(" +
                FavoriteMoviesContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " FLOAT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL); ";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }


    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {


    }
}
