package com.particular.marc.ghibliproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.List;

public class CustomLiveData extends MediatorLiveData<Pair<List<Movie>, String>>{
    private static final String TAG = "CustomLiveData";

    public CustomLiveData(LiveData<List<Movie>> moviesToUse, LiveData<String> filterToUse) {
        addSource(moviesToUse, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "onChanged: " + movies);
                setValue(Pair.create(movies, filterToUse.getValue()));
            }
        });
        addSource(filterToUse, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String filter) {
                Log.d(TAG, "onChanged: " + moviesToUse.getValue());
                setValue(Pair.create(moviesToUse.getValue(), filter));
            }
        });
    }

}