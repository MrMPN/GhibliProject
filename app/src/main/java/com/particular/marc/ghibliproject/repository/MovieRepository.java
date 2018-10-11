package com.particular.marc.ghibliproject.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.network.ApiRequest;
import com.particular.marc.ghibliproject.network.RetrofitClientInstance;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static final String TAG = "MovieRepository";
    private ApiRequest service;
    private static MovieRepository instance;
    private MutableLiveData<List<Movie>> data= new MutableLiveData<>();

    public static synchronized MovieRepository getInstance(){
        if (instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    public MovieRepository(){
        service = RetrofitClientInstance.getRetrofitInstance().create(ApiRequest.class);
    }

    public LiveData<List<Movie>> getMovies(){
        //final MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        Call<List<Movie>> call = service.getAllMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                List<Movie> list = response.body();
                data.setValue(list);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Movie>> sortMoviesByName(){
        List<Movie> movies = data.getValue();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });
        data.setValue(movies);
        return data;
    }

//    public LiveData<List<Movie>> sortMoviesByRating(){
//        List<Movie> movies = data.getValue();
//        Collections.sort(movies, new Comparator<Movie>() {
//            @Override
//            public int compare(Movie o1, Movie o2) {
//                return o1.getScore()
//            }
//        });
//        data.setValue(movies);
//        return data;
//    }


}
