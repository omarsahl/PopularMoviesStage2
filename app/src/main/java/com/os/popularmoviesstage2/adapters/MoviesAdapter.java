package com.os.popularmoviesstage2.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.models.Movie;
import com.os.popularmoviesstage2.models.MoviePreview;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Omar on 16-Feb-18 3:26 PM
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private final Context context;
    private final OnMovieClickListener onMovieClickListener;
    private List<MoviePreview> movies = null;

    public MoviesAdapter(Context context, OnMovieClickListener onMovieClickListener) {
        this.context = context;
        this.onMovieClickListener = onMovieClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movies_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MoviePreview movie = movies.get(position);
        Picasso.with(context)
                .load(context.getString(R.string.movies_db_poster_base_url_poster_w342) + movie.getPosterUrl())
                .placeholder(R.drawable.poster_ph)
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.size();
    }

    public void updateData(List<MoviePreview> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.listItemMoviePoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieClickListener.onMovieClicked(movies.get(getAdapterPosition()));
        }
    }

    public interface OnMovieClickListener {
        void onMovieClicked(MoviePreview movie);
    }
}
