package com.particular.marc.ghibliproject.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.particular.marc.ghibliproject.R;
import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.viewmodel.MainViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    private static final int UNLIKE = 1;
    private static final int LIKE = 2;
    private MainViewModel viewModel;
    private Movie movie;
    TextView titleView;
    TextView yearView;
    TextView descriptionView;
    TextView directorView;
    TextView producerView;
    TextView scoreView;
    ImageView favoriteView;


    public DetailFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        movie = viewModel.getSelectedMovie();
        setViews();
    }

    private void initViews(View view){
        titleView = view.findViewById(R.id.title);
        yearView = view.findViewById(R.id.year);
        descriptionView = view.findViewById(R.id.description);
        directorView = view.findViewById(R.id.director);
        producerView = view.findViewById(R.id.producer);
        scoreView = view.findViewById(R.id.score);
        favoriteView = view.findViewById(R.id.favorite);
    }

    private void setViews(){
        titleView.setText(movie.getTitle());
        yearView.setText(String.valueOf(movie.getReleaseYear()));
        descriptionView.setText(movie.getDescription());
        directorView.setText(movie.getDirector());
        producerView.setText(movie.getProducer());
        scoreView.setText(String.valueOf(movie.getScore()));
        if (movie.isFavorite()){
            favoriteView.setImageResource(R.drawable.like);
        }
        favoriteView.setOnClickListener(v -> changeFavoriteStatus());
    }

    private void changeFavoriteStatus(){
        if (movie.isFavorite()){
            movie.setFavorite(false);
            favoriteView.setImageResource(R.drawable.unlike);
        } else {
            movie.setFavorite(true);
            favoriteView.setImageResource(R.drawable.like);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.saveFavoriteStatus(movie);
    }
}
