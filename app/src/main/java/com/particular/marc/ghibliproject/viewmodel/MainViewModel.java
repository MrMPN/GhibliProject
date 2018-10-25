package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.util.Log;

import com.particular.marc.ghibliproject.helper.MovieComparatorHelper;
import com.particular.marc.ghibliproject.CustomLiveData;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    public static final String BY_NAME = "by_name";
    public static final String BY_RATING = "by_rating";
    public static final String BY_YEAR = "by_year";
    public static final String FAVORITES = "by_favorites";
    private MutableLiveData<String> filter = new MutableLiveData<>();
    private CustomLiveData trigger;
    private Movie selectedMovie;
    private MovieRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Constructor");
        repository = MovieRepository.getInstance(application);
        filter.setValue("START");
        trigger = new CustomLiveData(repository.getMovies(), filter);
    }

    public LiveData<List<Movie>> getMoviesFiltered(){
        Log.d(TAG, "getMoviesFiltered: ");
        return Transformations.map(trigger, value -> filterMovies(value.first, value.second));
    }

    private List<Movie> filterMovies(List<Movie> movies, final String filter) {
        if (movies != null) {
            switch (filter) {
                case "START":
                case BY_NAME:
                case BY_RATING:
                case BY_YEAR:
                    return sortMovies(movies, filter);
                case FAVORITES:
                    return selectFavorites(movies);
                default:
                    return getMatchingStrings(movies, filter);
            }
        }
        return null;
    }

    private List<Movie> getMatchingStrings(List<Movie> movies, String regex) {
        List<Movie> matches = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        if (TextUtils.isDigitsOnly(regex)){
            int year = Integer.parseInt(regex);
        } else {
            for (Movie m: movies) {
                if (p.matcher(m.getTitle()).matches()) {
                    matches.add(m);
                }
            }
        }
        return matches;
    }


    private List<Movie> sortMovies(List<Movie> movies, String filter){
        List<Movie> copy = new ArrayList<>(movies);
        Log.d(TAG, "filterMovies: Movies " + movies.toString());
        Collections.sort(copy, new MovieComparatorHelper(filter));
        return copy;
    }

    private List<Movie> selectFavorites(List<Movie> movies) {
        List<Movie> copy = new ArrayList<>();
        for (Movie m: movies){
            if (m.isFavorite()){
                copy.add(m);
            }
        }
        return copy;
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
}