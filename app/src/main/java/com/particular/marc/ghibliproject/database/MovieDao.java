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
}
