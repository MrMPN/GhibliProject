package com.particular.marc.ghibliproject;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.particular.marc.ghibliproject.model.Movie;
import com.particular.marc.ghibliproject.viewmodel.DetailFragmentViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    public static final String MOVIE_KEY = "movie_key";
    private static final int UNLIKE = 1;
    private static final int LIKE = 2;
    private Movie movie;
    TextView titleView;
    TextView yearView;
    TextView descriptionView;
    TextView directorView;
    TextView producerView;
    TextView scoreView;
    ImageView favoriteView;
    private DetailFragmentViewModel viewModel;

    public DetailFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(MOVIE_KEY);
        }
        initViews(view);
        setViews();
        viewModel = ViewModelProviders.of(getActivity()).get(DetailFragmentViewModel.class);

        return view;
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
        favoriteView.setTag(UNLIKE);
        if (movie.isFavorite()){
            favoriteView.setImageResource(R.drawable.like);
            favoriteView.setTag(LIKE);
        }

        favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavoriteStatus();
            }
        });
    }

    private void changeFavoriteStatus(){
        if (favoriteView.getTag().equals(UNLIKE)){
            movie.setFavorite(true);
            viewModel.insertFavorite(movie);
            favoriteView.setImageResource(R.drawable.like);
            favoriteView.setTag(LIKE);
        } else if (favoriteView.getTag().equals(LIKE)){
            movie.setFavorite(false);
            viewModel.deleteFavorite(movie);
            favoriteView.setImageResource(R.drawable.unlike);
            favoriteView.setTag(UNLIKE);
        }
    }
    
}
