package com.particular.marc.ghibliproject.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static final String TAG = "RetrofitClientInstance";
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://ghibliapi.herokuapp.com";
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}