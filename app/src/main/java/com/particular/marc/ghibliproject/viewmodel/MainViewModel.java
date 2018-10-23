package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import com.particular.marc.ghibliproject.helper.ComparatorHelper;
import com.particular.marc.ghibliproject.CustomLiveData;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

import java.util.Collections;
import java.util.List;

import static com.particular.marc.ghibliproject.fragment.MainFragment.ASC;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    private MovieRepository repository;
    private LiveData<List<Movie>> movies;
    private MutableLiveData<Integer> filter = new MutableLiveData<>();
    private CustomLiveData trigger;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Constructor");
        repository = MovieRepository.getInstance(application);
        movies = repository.getMovies();
        filter.setValue(ASC);
        trigger = new CustomLiveData(movies, filter);
    }

    public LiveData<List<Movie>> getMoviesFiltered(){
        return Transformations.map(trigger, value -> sortMoviesByName(value.first, value.second));
    }

    private List<Movie> sortMoviesByName(final List<Movie> movies ,final Integer filter){
        List<Movie> list = movies;
        if (list != null){
            Collections.sort(list, new ComparatorHelper.NameComparator(filter));
        }
        return list;
    }

    public void changeFilter(int filter){
        this.filter.setValue(filter);
    }

    public int getFilter(){
        return filter.getValue();
    }

//    public LiveData<List<Movie>> getMovies(){
//        return movies;
//    }
}
