package com.darko.themoviedbapp.database;


import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.darko.themoviedbapp.datamodel.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMovies(List<Movie> notes);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE id = :id")
    Movie getMovieById(int id);

    //ZARADI OVA NAJVEROJATNO GI DAVA VO RAZLICHEN ORDER OD TOA SHTO GI DAVA PREKU API-TO :D
    @Query("SELECT * FROM movies ORDER BY id DESC")
    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM movies")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM movies")
    int getCount();
}