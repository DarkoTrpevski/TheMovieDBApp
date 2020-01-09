package com.darko.themoviedbapp.ui.main;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.darko.themoviedbapp.database.MovieRepository;
import com.darko.themoviedbapp.datamodel.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application.getApplicationContext());
    }
    LiveData<List<Movie>> getAllMovies() {
        return movieRepository.getAllMovies();
    }
}