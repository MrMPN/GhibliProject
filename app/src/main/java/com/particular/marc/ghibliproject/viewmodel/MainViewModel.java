package com.particular.marc.ghibliproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    private MovieRepository repository;
    private LiveData<List<Movie>> data;
    private LiveData<List<Movie>> favs;
    private final MediatorLiveData<MyTaggedMovies> tagged = new MediatorLiveData<>();
    private final MyTaggedMovies current = new MyTaggedMovies();

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Constructor");
        repository = MovieRepository.getInstance(application);
        data = repository.getMovies();
        favs = repository.getFavorites();
        checkSources();
    }

    public void checkSources(){
        Log.d(TAG, "checkSources: ");
        tagged.addSource(data, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Mediator on Changed data:" + movies.toString());
                current.movies = movies;
                if (current.isComplete()){
                    current.tagMovies();
                }
                tagged.setValue(current);
            }
        });
        tagged.addSource(favs, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Mediator on Changed favs:" + movies.toString());
                current.favorites = movies;
                if (current.isComplete()){
                    current.tagMovies();
                }
                tagged.setValue(current);
            }
        });
    }

    public MediatorLiveData<MyTaggedMovies> getTagged(){return tagged;}

    public void sortByName(final int order) {
        repository.sortMoviesByName(order);
    }

    public void sortByRating(final int order){
        repository.sortMoviesByRating(order);
    }

    public void sortByYear(final int order){
        repository.sortMoviesByYear(order);
    }


    public class MyTaggedMovies {

        private List<Movie> movies;
        private List<Movie> favorites;

        MyTaggedMovies() {}

        boolean isComplete() {
            return (movies != null && favorites != null);
        }

        void tagMovies(){
            for (Movie m: movies){
                for (Movie f : favorites){
                    if (m.getId().equals(f.getId())){
                        m.setFavorite(true);
                    }
                }
            }
        }

        public List<Movie> getMovies() {
            return movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies = movies;
        }

        public List<Movie> getFavorites() {
            return favorites;
        }

        public void setFavorites(List<Movie> favorites) {
            this.favorites = favorites;
        }
    }
}
