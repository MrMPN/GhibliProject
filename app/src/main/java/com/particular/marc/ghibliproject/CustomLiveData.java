package com.particular.marc.ghibliproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.List;

public class CustomLiveData extends MediatorLiveData<Pair<List<Movie>, Integer>>{
    public CustomLiveData(LiveData<List<Movie>> moviesToUse, LiveData<Integer> filterToUse) {
        addSource(moviesToUse, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                setValue(Pair.create(movies, filterToUse.getValue()));
            }
        });
        addSource(filterToUse, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer filter) {
                setValue(Pair.create(moviesToUse.getValue(), filter));
            }
        });
    }

}