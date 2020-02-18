package com.darko.themoviedbapp.ui.main;


import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darko.themoviedbapp.R;
import com.darko.themoviedbapp.datamodel.Movie;
import com.darko.themoviedbapp.ui.main.adapter.MoviesAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivityTAG";

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INIT RECYCLER AND VIEW MODEL
        initRecyclerAndViewModel();
    }

    private void initRecyclerAndViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        moviesAdapter = new MoviesAdapter(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getPopularMovies();
    }

    //OBSERVE CHANGES TO THE VIEW MODEL
    private void getPopularMovies() {
        try {
            //WE WILL OBSERVE THE NOTES(SUBSCRIBED TO THE DATA)
            mainViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> moviesFromLiveData) {
                    moviesAdapter.setMovieList(moviesFromLiveData);
                    showOnRecyclerAndUpdateDB();
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "loadMoviesFromAPI: Error" + e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showOnRecyclerAndUpdateDB() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesAdapter);
    }

    //CREATE MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}