package com.particular.marc.ghibliproject.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.particular.marc.ghibliproject.R;
import com.particular.marc.ghibliproject.RecyclerViewAdapter;
import com.particular.marc.ghibliproject.RecyclerViewAdapter.ListItemClickListener;
import com.particular.marc.ghibliproject.helper.MovieComparatorHelper;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.viewmodel.MainViewModel;

import java.util.List;

import static com.particular.marc.ghibliproject.helper.MovieComparatorHelper.ASC;
import static com.particular.marc.ghibliproject.helper.MovieComparatorHelper.DESC;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements ListItemClickListener {
    private static final String TAG = "MainFragment";
    private RecyclerViewAdapter adapter;
    private MainViewModel viewModel;

    public MainFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        setRecyclerView(v);
        getActivity().setTitle(R.string.app_name);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (item.getItemId()){
            case R.id.search:
                break;
            case R.id.go_to_favorite:
                FavoriteFragment fragment = new FavoriteFragment();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            case R.id.sort_by_name:
                int nameSortOrder = sharedPreferences.getInt(getString(R.string.sort_by_name_key), ASC);
                nameSortOrder = (nameSortOrder == ASC) ? DESC : ASC;
                viewModel.filterBy("by_name");
                editor.putInt(getString(R.string.sort_by_name_key), nameSortOrder);
                break;
            case R.id.sort_by_rating:
                int ratingSortOrder = sharedPreferences.getInt(getString(R.string.sort_by_rating_key), DESC);
                viewModel.filterBy("by_rating");
                ratingSortOrder = (ratingSortOrder == ASC) ? DESC : ASC;
                editor.putInt(getString(R.string.sort_by_rating_key), ratingSortOrder);
                break;
            case R.id.sort_by_year:
                viewModel.filterBy("Arrietty");
                break;
        }
        editor.apply();
        return true;
    }
}
