package com.particular.marc.ghibliproject.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.particular.marc.ghibliproject.helper.AppExecutors;
import com.particular.marc.ghibliproject.database.AppDatabase;
import com.particular.marc.ghibliproject.database.MovieDao;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.network.ApiRequest;
import com.particular.marc.ghibliproject.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static final String TAG = "MovieRepository";
    private ApiRequest service;
    private MovieDao movieDao;
    private static MovieRepository instance;
    private final LiveData<List<Movie>> movies;
    private AppExecutors mExecutors = AppExecutors.getInstance();

    public static synchronized MovieRepository getInstance(Application application){
        if (instance == null){
            Log.d(TAG, "getInstance: new Instance of MovieRepository");
            instance = new MovieRepository(application);
        }
        return instance;
    }

    private MovieRepository(Application application){
        Log.d(TAG, "MovieRepository: Initializing");
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        service = RetrofitClientInstance.getRetrofitInstance().create(ApiRequest.class);
        movieDao = db.movieDao();
        movies = movieDao.getMovies();
    }

    public LiveData<List<Movie>> getMovies(){
        if (movies.getValue() == null || movies.getValue().isEmpty()){
            fetchMovies();
        }
        return movies;
    }

    private void fetchMovies(){
        Call<List<Movie>> call = service.getAllMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                Log.d(TAG, "onResponse:");
                final List<Movie> list = response.body();
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        movieDao.insert(list);
                    }
                });
            }
            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void saveFavoriteStatus(final Movie movie){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.update(movie);
            }
        });
    }

}
