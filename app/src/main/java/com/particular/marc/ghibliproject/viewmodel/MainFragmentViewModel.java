package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

import java.util.List;

public class MainFragmentViewModel extends AndroidViewModel {
    private MovieRepository repository;
    private LiveData<List<Movie>> data;
    private LiveData<List<Movie>> favs;

    public MainFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance(application);
        data = repository.getMovies();
        favs = repository.getFavorites();
    }

    public LiveData<List<Movie>> getMovies() {
        return data;
    }

    public LiveData<List<Movie>> getFavorites(){return favs;}

    public void sortByName(final int order) {
        repository.sortMoviesByName(order);
    }

    public void sortByRating(final int order){
        repository.sortMoviesByRating(order);
    }

    public void sortByYear(final int order){
        repository.sortMoviesByYear(order);
    }

    public MediatorLiveData<MovieRepository.MyTaggedMovies> checkSources(){return repository.checkSources();}

}
