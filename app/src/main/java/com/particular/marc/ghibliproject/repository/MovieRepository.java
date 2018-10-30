package com.particular.marc.ghibliproject.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.particular.marc.ghibliproject.fragment.MainFragment;
import com.particular.marc.ghibliproject.helper.AppExecutors;
import com.particular.marc.ghibliproject.database.AppDatabase;
import com.particular.marc.ghibliproject.database.MovieDao;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.network.ApiRequest;
import com.particular.marc.ghibliproject.network.RetrofitClientInstance;
import com.particular.marc.ghibliproject.viewmodel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static final String TAG = "MovieRepository";
    private ApiRequest service;
    private MovieDao movieDao;
    private static MovieRepository instance;
    private LiveData<List<Movie>> movies;
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
        initData(application);
    }

    private void initData(Application application){
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        service = RetrofitClientInstance.getRetrofitInstance().create(ApiRequest.class);
        movieDao = db.movieDao();
        this.movies = movieDao.getMoviesSorted(MainViewModel.BY_NAME_ASC);
        if (movies.getValue() == null || movies.getValue().isEmpty()){
            fetchMovies();
        }
    }

    public LiveData<List<Movie>> getMoviesSorted(String sort){
        return movieDao.getMoviesSorted(sort);
    }

    public LiveData<List<Movie>> searchMovies(String query){
        if (TextUtils.isDigitsOnly(query)){
            query = "%" + query + "%";
            return movieDao.searchByYear(query);
        }
        query = "%" + query + "%";
        return movieDao.searchByTitle(query);
    }

    public LiveData<List<Movie>> getFavorites(){
        return movieDao.getFavorites();
    }

    /**
     * Method to fetch movies from the Ghibli API.
     * If the call to the API is succesful we update the database
     */

    private void fetchMovies(){
        Call<List<Movie>> call = service.getAllMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                Log.d(TAG, "onResponse:");
                final List<Movie> list = response.body();
                mExecutors.diskIO().execute(() -> movieDao.insert(list));
            }
            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void saveFavoriteStatus(final Movie movie){
        mExecutors.diskIO().execute(() -> movieDao.update(movie));
    }

}
