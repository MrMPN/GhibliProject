package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import com.particular.marc.ghibliproject.CustomLiveData;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    public static final String BY_NAME_ASC = "by_name";
    public static final String BY_NAME_DESC ="by_name_desc";
    public static final String BY_RATING_ASC = "by_rating";
    public static final String BY_RATING_DESC ="by_rating_desc";
    public static final String BY_YEAR_ASC = "by_year";
    public static final String BY_YEAR_DESC ="by_year_desc";
    public static final String FAVORITES = "by_favorites";

    private MutableLiveData<String> filter = new MutableLiveData<>();
    private CustomLiveData trigger;
    private Movie selectedMovie;
    private MovieRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Constructor");
        repository = MovieRepository.getInstance(application);
        filter.setValue("");
        trigger = new CustomLiveData(repository.getMoviesSorted(MainViewModel.BY_NAME_ASC), filter);
    }

    public LiveData<List<Movie>> getMoviesFiltered(){
        Log.d(TAG, "getMoviesFiltered: ");
        return Transformations.switchMap(trigger, value -> filterMovies(value.first, value.second));
    }

    private LiveData<List<Movie>> filterMovies(List<Movie> movies, final String filter) {
        switch (filter) {
            case "":
                return repository.getMoviesSorted(MainViewModel.BY_NAME_ASC);
            case BY_NAME_ASC:
            case BY_NAME_DESC:
            case BY_RATING_ASC:
            case BY_RATING_DESC:
            case BY_YEAR_ASC:
            case BY_YEAR_DESC:
                return repository.getMoviesSorted(filter);
            case FAVORITES:
                return repository.getFavorites();
            default:
                return repository.searchMovies(filter);
        }
    }

    public void filterBy(String filter){
        this.filter.setValue(filter);
    }

    public void setSelectedMovie(Movie selected){
        selectedMovie = selected;
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void saveFavoriteStatus(Movie movie){
        repository.saveFavoriteStatus(movie);
    }

    public String getFilter() {
        return filter.getValue();
    }
}