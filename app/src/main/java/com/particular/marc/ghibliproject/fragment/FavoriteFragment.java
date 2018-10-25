package com.particular.marc.ghibliproject.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.particular.marc.ghibliproject.R;
import com.particular.marc.ghibliproject.RecyclerViewAdapter;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.viewmodel.MainViewModel;

import java.util.List;

public class FavoriteFragment extends Fragment implements RecyclerViewAdapter.ListItemClickListener {
    private static final String TAG = "FavoriteFragment";
    private RecyclerViewAdapter adapter;
    private MainViewModel viewModel;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        setRecyclerView(v);
        getActivity().setTitle(R.string.your_favorites);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }

    private void setRecyclerView(View v){
        RecyclerView recyclerView = v.findViewById(R.id.list);
        adapter = new RecyclerViewAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initViewModel(){
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.filterBy(MainViewModel.FAVORITES);
        viewModel.getMoviesFiltered().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "onChanged: LiveData changed");
                adapter.swap(movies);
            }
        });
    }


    @Override
    public void onListItemClick(Movie clickedItem) {
        viewModel.setSelectedMovie(clickedItem);
        DetailFragment fragment = new DetailFragment();
        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onStop() {
        viewModel.filterBy("");
        super.onStop();
    }
}
