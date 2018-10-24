package com.particular.marc.ghibliproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.List;

public class CustomLiveData extends MediatorLiveData<Pair<List<Movie>, Pair<Integer,Integer>>>{
    private static final String TAG = "CustomLiveData";

    public CustomLiveData(LiveData<List<Movie>> moviesToUse, LiveData<Pair<Integer,Integer>> filterToUse) {
        addSource(moviesToUse, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "onChanged: " + movies);
                setValue(Pair.create(movies, filterToUse.getValue()));
            }
        });
        addSource(filterToUse, new Observer<Pair<Integer,Integer>>() {
            @Override
            public void onChanged(@Nullable Pair<Integer,Integer> filter) {
                Log.d(TAG, "onChanged: " + moviesToUse.getValue());
                setValue(Pair.create(moviesToUse.getValue(), filter));
            }
        });
    }

}