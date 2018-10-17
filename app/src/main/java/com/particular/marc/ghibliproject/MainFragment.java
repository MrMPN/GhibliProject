package com.particular.marc.ghibliproject;


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

import com.particular.marc.ghibliproject.RecyclerViewAdapter.ListItemClickListener;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.repository.MovieRepository;
import com.particular.marc.ghibliproject.viewmodel.MainViewModel;

import java.util.List;

import static com.particular.marc.ghibliproject.DetailFragment.MOVIE_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements ListItemClickListener {
    private static final String TAG = "MainFragment";
    public static int ASC = 1;
    public static int DESC = 2;
    private static int nameSortOrder = ASC;
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
        viewModel.getTagged().observe(getViewLifecycleOwner(), new Observer<MainViewModel.MyTaggedMovies>() {
            @Override
            public void onChanged(@Nullable MainViewModel.MyTaggedMovies myTaggedMovies) {
                Log.d(TAG, "onChanged: myTaggedMovies");
                adapter.swap(myTaggedMovies.getMovies());
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
                viewModel.sortByName(nameSortOrder);
                nameSortOrder = (nameSortOrder == ASC) ? DESC : ASC;
                break;
            case R.id.sort_by_rating:
                viewModel.sortByRating(ratingSortOrder);
                ratingSortOrder = (ratingSortOrder == ASC) ? DESC : ASC;
                break;
            case R.id.sort_by_year:
                viewModel.sortByYear(yearSortOrder);
                yearSortOrder = (yearSortOrder == ASC) ? DESC : ASC;
                break;
        }
        return true;
    }
}
