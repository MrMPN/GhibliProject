package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    private MovieRepository repository;
    private LiveData<List<Movie>> data;
    private LiveData<List<Movie>> favs;
    private MediatorLiveData<MovieRepository.MyTaggedMovies> tagged;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Constructor");
        repository = MovieRepository.getInstance(application);
        data = repository.getMovies();
        favs = repository.getFavorites();
        tagged = repository.getTagged();
    }

    public LiveData<List<Movie>> getMovies() {
        return data;
    }

    public LiveData<List<Movie>> getFavorites(){return favs;}

    public MediatorLiveData<MovieRepository.MyTaggedMovies> getTagged(){return tagged;}

    public void sortByName(final int order) {
        repository.sortMoviesByName(order);
    }

    public void sortByRating(final int order){
        repository.sortMoviesByRating(order);
    }

    public void sortByYear(final int order){
        repository.sortMoviesByYear(order);
    }
}
