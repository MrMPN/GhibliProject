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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.particular.marc.ghibliproject.R;
import com.particular.marc.ghibliproject.RecyclerViewAdapter;
import com.particular.marc.ghibliproject.RecyclerViewAdapter.ListItemClickListener;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.viewmodel.MainViewModel;

import java.util.List;

import static com.particular.marc.ghibliproject.fragment.DetailFragment.MOVIE_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements ListItemClickListener {
    private static final String TAG = "MainFragment";
    public static int ASC = 0;
    public static int DESC = 1;
    private static int ratingSortOrder = DESC;
    private static int yearSortOrder = ASC;
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
        Log.d(TAG, "initViewModel: ");
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.getMoviesFiltered().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "onChanged: ");
                adapter.swap(movies);
            }
        });
    }

    @Override
    public void onListItemClick(Movie clickedItem) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(MOVIE_KEY, clickedItem);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);
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
        switch (item.getItemId()){
            case R.id.search:
                break;
            case R.id.go_to_favorite:
                break;
            case R.id.sort_by_name:
                int nameSortOrder = (viewModel.getFilter() == ASC) ? DESC : ASC;
                viewModel.changeFilter(nameSortOrder);
                break;
            case R.id.sort_by_rating:
                ratingSortOrder = (ratingSortOrder == ASC) ? DESC : ASC;
                break;
            case R.id.sort_by_year:
                yearSortOrder = (yearSortOrder == ASC) ? DESC : ASC;
                break;
        }
        return true;
    }
}
