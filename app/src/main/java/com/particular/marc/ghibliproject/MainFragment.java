package com.particular.marc.ghibliproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.particular.marc.ghibliproject.RecyclerViewAdapter.ListItemClickListener;
import com.particular.marc.ghibliproject.domain.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements ListItemClickListener {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;


    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        List<Movie> list = new ArrayList<>();
        list.add(new Movie("1", "Mononoke", "POTATO", "Hayao Miyazaki", "Senyor Random", 1997, 95 ));
        list.add(new Movie("2", "Castell Ambulant", "LALALALALA", "Hayao Miyazaki", "Senyor Random 2", 1987, 83 ));
        recyclerView = v.findViewById(R.id.list);
        adapter = new RecyclerViewAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setMovies(list);

        return v;
    }

    @Override
    public void onListItemClick(Movie clickedItem) {
        DetailFragment fragment = new DetailFragment();
        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }
}
