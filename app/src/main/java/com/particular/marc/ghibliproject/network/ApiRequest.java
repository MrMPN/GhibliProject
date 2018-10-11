package com.particular.marc.ghibliproject.network;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequest {

    @GET("films")
    Call<List<Movie>> getAllMovies();
}
