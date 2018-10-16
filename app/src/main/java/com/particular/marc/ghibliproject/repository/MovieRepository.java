package com.particular.marc.ghibliproject.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.particular.marc.ghibliproject.AppExecutors;
import com.particular.marc.ghibliproject.database.AppDatabase;
import com.particular.marc.ghibliproject.database.MovieDao;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.network.ApiRequest;
import com.particular.marc.ghibliproject.network.RetrofitClientInstance;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.particular.marc.ghibliproject.MainFragment.ASC;

public class MovieRepository {
    private static final String TAG = "MovieRepository";
    private ApiRequest service;
    private MovieDao movieDao;
    private static MovieRepository instance;
    private final MutableLiveData<List<Movie>> data = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> favs = new MutableLiveData<>();
    private final MediatorLiveData<MyTaggedMovies> tagged = new MediatorLiveData<>();
    private AppExecutors mExecutors = AppExecutors.getInstance();

    public static synchronized MovieRepository getInstance(Application application){
        if (instance == null){
            instance = new MovieRepository(application);
        }
        return instance;
    }

    private MovieRepository(Application application){
        Log.d(TAG, "MovieRepository: Initializing");
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        service = RetrofitClientInstance.getRetrofitInstance().create(ApiRequest.class);
        movieDao = db.movieDao();
        fetchMovies();
        fetchFavorites();
        checkSources();
    }

    private void fetchMovies(){
        Call<List<Movie>> call = service.getAllMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                List<Movie> list = response.body();
                data.setValue(list);
            }
            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                data.setValue(null);
            }
        });
    }

    public void fetchFavorites(){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favs.postValue(movieDao.getFavorites());
            }
        });
    }

    public void checkSources(){
        Log.d(TAG, "checkSources: ");
        final MyTaggedMovies current = new MyTaggedMovies();
        tagged.addSource(data, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Mediator on Changed data:" + movies.toString());
                current.movies = movies;
                tagged.setValue(current);
            }
        });
        tagged.addSource(favs, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Mediator on Changed favs:" + movies.toString());
                current.favorites = movies;
                tagged.setValue(current);
            }
        });
    }


    public LiveData<List<Movie>> getMovies(){
        return data;
    }

    public LiveData<List<Movie>> getFavorites(){
        return favs;
    }

    public MediatorLiveData<MyTaggedMovies> getTagged(){
        return tagged;}

    public void insertFavorite(final Movie movie){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.insert(movie);
            }
        });
    }

    public void deleteFavorite(final Movie movie){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.delete(movie);
            }
        });
    }

    public void sortMoviesByName(final int order){
        List<Movie> movies = data.getValue();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (order == ASC){
                    return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                } else {
                    return -1 * o1.getTitle().compareToIgnoreCase(o2.getTitle());
                }
            }
        });
        data.setValue(movies);
    }

    public void sortMoviesByRating(final int order){
        List<Movie> movies = data.getValue();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (order == ASC){
                    return Integer.compare(o1.getScore(), o2.getScore());
                } else {
                    return -1 * Integer.compare(o1.getScore(), o2.getScore());
                }
            }
        });
        data.setValue(movies);
    }

    public void sortMoviesByYear(final int order){
        List<Movie> movies = data.getValue();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (order == ASC){
                    return Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
                } else {
                    return -1 * Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
                }
            }
        });
        data.setValue(movies);
    }


    public class MyTaggedMovies {

        List<Movie> movies;
        List<Movie> favorites;

        public MyTaggedMovies() {}

        public boolean isComplete() {
            return (movies != null && favorites != null);
        }

        public void tagMovies(){
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
