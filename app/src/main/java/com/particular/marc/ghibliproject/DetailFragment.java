package com.particular.marc.ghibliproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.particular.marc.ghibliproject.domain.Movie;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
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

    public DetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(MOVIE_KEY);
        }
        initViews(view);
        setViews();

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
                if (favoriteView.getTag().equals(UNLIKE)){
                    favoriteView.setImageResource(R.drawable.like);
                    favoriteView.setTag(LIKE);
                } else if (favoriteView.getTag().equals(LIKE)){
                    favoriteView.setImageResource(R.drawable.unlike);
                    favoriteView.setTag(UNLIKE);
                }
            }
        });
    }

}
