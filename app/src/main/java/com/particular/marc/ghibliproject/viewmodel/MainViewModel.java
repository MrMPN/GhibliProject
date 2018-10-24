package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import com.particular.marc.ghibliproject.helper.MovieComparatorHelper;
import com.particular.marc.ghibliproject.CustomLiveData;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    private MutableLiveData<Pair<Integer,Integer>> filter = new MutableLiveData<>();
    private CustomLiveData trigger;
    private Movie selectedMovie;
    private MovieRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Constructor");
        repository = MovieRepository.getInstance(application);
        trigger = new CustomLiveData(repository.getMovies(), filter);
    }

    public LiveData<List<Movie>> getMoviesFiltered(){
        return Transformations.map(trigger, value -> sortMoviesByName(value.first, value.second));
    }

    private List<Movie> sortMoviesByName(List<Movie> movies ,final Pair<Integer,Integer> filter){
        List<Movie> copy = new ArrayList<>(movies);
        Log.d(TAG, "sortMoviesByName: Movies " + movies.toString());
        if (!copy.isEmpty()){
            Collections.sort(copy, new MovieComparatorHelper(filter));
        }
        return copy;
    }

    public void filterBy(int filter, int order){
        this.filter.setValue(new Pair<>(filter, order));
    }

    public void setSelectedMovie(Movie selected){
        selectedMovie = selected;
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void saveFavoriteStatus(Movie movie){
        Log.d(TAG, "saveFavoriteStatus: ");
        repository.saveFavoriteStatus(movie);
    }
}