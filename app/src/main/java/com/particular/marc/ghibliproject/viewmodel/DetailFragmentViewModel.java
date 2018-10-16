package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

public class DetailFragmentViewModel extends AndroidViewModel {
    private static final String TAG = "DetailFragmentViewModel";
    private MovieRepository repository;

    public DetailFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance(application);
    }

    public void insertFavorite(Movie movie){
        Log.d(TAG, "insertFavorite: ");
        repository.insertFavorite(movie);
    }

    public void deleteFavorite(Movie movie){
        Log.d(TAG, "deleteFavorite: ");
        repository.deleteFavorite(movie);
    }

}
