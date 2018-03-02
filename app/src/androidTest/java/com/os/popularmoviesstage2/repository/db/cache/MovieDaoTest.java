package com.os.popularmoviesstage2.repository.db.cache;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.os.popularmoviesstage2.models.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Omar on 26-Feb-18 7:08 PM
 */

@RunWith(AndroidJUnit4.class)
public class MovieDaoTest {
    private AllMoviesCacheDb moviesDb;
    private Movie movie;

    @Before
    public void setUp() throws Exception {
        movie = new Movie();
        movie.setId(47);
        movie.setTitle("John Wick");
        movie.setOverview("John Wick Movie");
        movie.setVoteCount(34343);
        movie.setReleaseDate("2017");
        movie.setRating(9.4f);
        movie.setDuration(102);
        movie.setPosterUrl("posterUrl");
        movie.setBackDropUrl("backdropUrl");

        moviesDb = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AllMoviesCacheDb.class)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        moviesDb.close();
    }

    @Test
    public void getMovieById() throws Exception {

    }

    @Test
    public void insertAllMovies() throws Exception {
    }

    @Test
    public void insertMovie() throws Exception {
        moviesDb.movieDao().insertMovie(movie);
        moviesDb.movieDao().getMovieById(47)
                .test()
                .assertValue(movie -> {
                    System.out.println(movie);
                    return movie.getDuration() == 102 && movie.getRating() == 9.4f;
                });
    }

}