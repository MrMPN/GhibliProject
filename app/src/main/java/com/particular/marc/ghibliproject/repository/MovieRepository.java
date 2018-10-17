package com.particular.marc.ghibliproject.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.particular.marc.ghibliproject.AppExecutors;
import com.particular.marc.ghibliproject.database.AppDatabase;
import com.particular.marc.ghibliproject.database.MovieDao;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.network.ApiRequest;
import com.particular.marc.ghibliproject.network.RetrofitClientInstance;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.particular.marc.ghibliproject.MainFragment.ASC;

public class MovieRepository {
    private static final String TAG = "MovieRepository";
    private ApiRequest service;
    private MovieDao movieDao;
    private static MovieRepository instance;
    private final MutableLiveData<List<Movie>> data = new MutableLiveData<>();
    private LiveData<List<Movie>> favs;
    private AppExecutors mExecutors = AppExecutors.getInstance();

    public static synchronized MovieRepository getInstance(Application application){
        if (instance == null){
            Log.d(TAG, "getInstance: new Instance");
            instance = new MovieRepository(application);
        }
        return instance;
    }

    private MovieRepository(Application application){
        Log.d(TAG, "MovieRepository: Initializing");
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        service = RetrofitClientInstance.getRetrofitInstance().create(ApiRequest.class);
        movieDao = db.movieDao();
        fetchMovies();
        favs = movieDao.getFavorites();
    }

    private void fetchMovies(){
        Call<List<Movie>> call = service.getAllMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                Log.d(TAG, "onResponse:");
                List<Movie> list = response.body();
                data.setValue(list);
            }
            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                data.setValue(null);
            }
        });
    }

    public LiveData<List<Movie>> getMovies(){
        return data;
    }

    public LiveData<List<Movie>> getFavorites(){
        return favs;
    }

    public void insertFavorite(final Movie movie){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.insert(movie);
            }
        });
    }

    public void deleteFavorite(final Movie movie){
        Log.d(TAG, "deleteFavorite: ");
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.delete(movie);
            }
        });
    }

    public void sortMoviesByName(final int order){
        List<Movie> movies = data.getValue();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (order == ASC){
                    return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                } else {
                    return -1 * o1.getTitle().compareToIgnoreCase(o2.getTitle());
                }
            }
        });
        data.setValue(movies);
    }

    public void sortMoviesByRating(final int order){
        List<Movie> movies = data.getValue();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (order == ASC){
                    return Integer.compare(o1.getScore(), o2.getScore());
                } else {
                    return -1 * Integer.compare(o1.getScore(), o2.getScore());
                }
            }
        });
        data.setValue(movies);
    }

    public void sortMoviesByYear(final int order){
        List<Movie> movies = data.getValue();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (order == ASC){
                    return Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
                } else {
                    return -1 * Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
                }
            }
        });
        data.setValue(movies);
    }


}
