package com.darko.themoviedbapp.database;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.darko.themoviedbapp.BuildConfig;
import com.darko.themoviedbapp.datamodel.Movie;
import com.darko.themoviedbapp.datamodel.MoviesResponse;
import com.darko.themoviedbapp.network.MovieRetrofitClient;
import com.darko.themoviedbapp.network.MovieRetrofitService;
import com.darko.themoviedbapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private Context context;

    private static MovieRepository ourInstance;
    private AppDatabase database;

    private List<Movie> movieArrayList = new ArrayList<>();
    private MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

    private Executor executor = Executors.newSingleThreadExecutor();

//--------------------------------------------------------------------------------------------------
    //MovieRepository SINGLETON CONSTRUCTOR
    public static MovieRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MovieRepository(context);
        }
        return ourInstance;
    }

    //MovieRepository CONSTRUCTOR
    private MovieRepository(Context context) {
        this.context = context;
        database = AppDatabase.getInstance(context);
    }
//--------------------------------------------------------------------------------------------------

    public LiveData<List<Movie>> getAllMovies() {
        LiveData<List<Movie>> movieListLiveData;
        if (NetworkUtils.isConnectionAvailable(context)) {
            //IF THERE IS A CONNECTION, LOAD THE MOVIES FROM API
            movieListLiveData = getMovieListFromApi();
        } else {
            //IF THERE IS A NO CONNECTION, LOAD THE MOVIES FROM DB
            movieListLiveData = getMovieListFromDB();
        }
        return movieListLiveData;
    }

    private LiveData<List<Movie>> getMovieListFromApi() {
        MovieRetrofitService apiMovieRetrofitService = MovieRetrofitClient.getClient().create(MovieRetrofitService.class);
        Call<MoviesResponse> call = apiMovieRetrofitService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                movieArrayList = response.body().getResults();
                mutableLiveData.setValue(movieArrayList);
                //UPDATE THE DATABASE
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        database.movieDao().insertAllMovies(movieArrayList);
                    }
                });
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
            }
        });
        return mutableLiveData;
    }

    private LiveData<List<Movie>> getMovieListFromDB() {
        return database.movieDao().getAllMovies();
    }
}
