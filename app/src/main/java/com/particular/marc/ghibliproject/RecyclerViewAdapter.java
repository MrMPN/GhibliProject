package com.particular.marc.ghibliproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    final private ListItemClickListener mOnClickListener;
    private List<Movie> mMovies;
    private LayoutInflater inflater;

    public interface ListItemClickListener{
        void onListItemClick (Movie clickedItem);
    }

    public RecyclerViewAdapter(Context mContext, ListItemClickListener listener) {
        mOnClickListener = listener;
        inflater = LayoutInflater.from(mContext);
    }

    void setMovies(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void swap(List<Movie> movies) {
        if (mMovies != null) {
            mMovies = null;
            mMovies = movies;
        }
        else {
            mMovies = movies;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "OnBindViewHolder method called");
        final Movie movie = mMovies.get(position);
        cleanHolder(holder);
        setViews(holder, movie);
    }

    private void cleanHolder(ViewHolder holder){
        holder.favorite.setVisibility(View.GONE);
    }

    private void setViews(ViewHolder holder, Movie movie){
        holder.titleTextView.setText(movie.getTitle());
        holder.releaseDate.setText(String.valueOf(movie.getReleaseYear()));
        holder.score.setText(String.valueOf(movie.getScore()));
        if (movie.isFavorite()){ holder.favorite.setVisibility(View.VISIBLE); }
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        TextView score;
        ImageView favorite;
        TextView titleTextView;
        TextView releaseDate;

        ViewHolder(View itemView) {
            super(itemView);
            score = itemView.findViewById(R.id.list_score);
            favorite = itemView.findViewById(R.id.list_favorite);
            titleTextView = itemView.findViewById(R.id.list_title);
            releaseDate = itemView.findViewById(R.id.list_year);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie clickedMovie = mMovies.get(getAdapterPosition());
            mOnClickListener.onListItemClick(clickedMovie);
        }
    }
}
