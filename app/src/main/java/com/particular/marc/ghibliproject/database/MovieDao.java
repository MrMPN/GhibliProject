package com.particular.marc.ghibliproject.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Movie> movies);

    @Delete
    void delete(Movie movie);

    @Update
    void update(Movie movie);

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getMovies();


    //FILTER queries
    @Query("SELECT * FROM movie ORDER BY " +
            "CASE WHEN :sort = 'by_name' THEN title " +
            "WHEN :sort = 'by_year' THEN releaseYear " +
            "WHEN :sort = 'by_rating' THEN score END ASC")
    LiveData<List<Movie>> sortMovies(String sort);

    @Query ("SELECT * FROM movie WHERE favorite = 1")
    LiveData<List<Movie>> getFavorites();


    //SEARCH queries
    @Query ("SELECT * FROM movie WHERE title LIKE :query")
    LiveData<List<Movie>> searchByTitle(String query);

    @Query ("SELECT * FROM movie WHERE CAST(releaseYear AS VARCHAR(8)) LIKE :query")
    LiveData<List<Movie>> searchByYear(String query);

}
