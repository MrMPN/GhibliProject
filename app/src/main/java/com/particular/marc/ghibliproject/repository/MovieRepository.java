package com.particular.marc.ghibliproject.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
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
    private AppExecutors mExecutors = AppExecutors.getInstance();

    public static synchronized MovieRepository getInstance(Application application){
        if (instance == null){
            instance = new MovieRepository(application);
        }
        return instance;
    }

    private MovieRepository(Application application){
        service = RetrofitClientInstance.getRetrofitInstance().create(ApiRequest.class);
        fetchMovies();
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        movieDao = db.movieDao();
    }

    private void fetchMovies(){
        Call<List<Movie>> call = service.getAllMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                List<Movie> list = response.body();
                data.setValue(list);
                fetchFavorites();
            }
            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                data.setValue(null);
            }
        });
    }

    public void fetchFavorites(){
        final List<Movie> movies = data.getValue();
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> favorites = movieDao.getFavorites();
                tagFavorites(favorites, movies);
            }
        });
    }

    private void tagFavorites(List<Movie> favorites, List<Movie> movies){
        for (Movie m: movies){
            for (Movie f : favorites){
                if (m.getId().equals(f.getId())){
                    m.setFavorite(true);
                }
            }
        }
        data.postValue(movies);
    }


    public LiveData<List<Movie>> getMovies(){
        return data;
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
