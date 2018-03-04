package com.os.popularmoviesstage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.databinding.FavoriteMoviesListItemBinding;
import com.os.popularmoviesstage2.models.MoviePreview;
import com.squareup.picasso.Picasso;

import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_BACKDROP_URL;
import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_ID;
import static com.os.popularmoviesstage2.repository.db.favorites.FavoriteMoviesDbContract.FavoritesEntry.COLUMN_NAME_TITLE;

/**
 * Created by Omar on 03-Mar-18 10:40 PM
 */

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMovieViewHolder> {
    private static final String TAG = FavoriteMoviesAdapter.class.getSimpleName();

    private OnFavoriteMovieClickedListener listener;
    private Context context;
    private Cursor cursor;

    public FavoriteMoviesAdapter(OnFavoriteMovieClickedListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        FavoriteMoviesListItemBinding itemBinding = FavoriteMoviesListItemBinding.inflate(inflater, parent, false);
        return new FavoriteMovieViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieViewHolder holder, int position) {
        MoviePreview movie = getMovieFromCursor(cursor, position);
        holder.bind(movie);
    }

    private MoviePreview getMovieFromCursor(Cursor cursor, int position) {
        boolean success = cursor.moveToPosition(position);
        MoviePreview movie;

        if (success) {
            movie = new MoviePreview();
            movie.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
            movie.setBackdropUrl(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_BACKDROP_URL)));
            return movie;
        } else {
            Log.e(TAG, "getMovieFromCursor: returning null instead of moviePreview!");
            return null;
        }
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    public void updateCursor(Cursor c) {
        if (c != null) {
            this.cursor = c;
            this.notifyDataSetChanged();
        }
    }

    public class FavoriteMovieViewHolder extends RecyclerView.ViewHolder {
        private FavoriteMoviesListItemBinding itemBinding;

        public FavoriteMovieViewHolder(FavoriteMoviesListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(MoviePreview movie) {
            Picasso.with(context)
                    .load(context.getString(R.string.movies_db_poster_base_url_backdrop_w780) + movie.getBackdropUrl())
                    .into(itemBinding.favoriteMovieBackdrop);
            itemBinding.setMovie(movie);
            itemBinding.setHandlers(this);
            itemBinding.executePendingBindings();
        }

        public void onClickFavoriteMovie(View v) {
            listener.onFavoriteMovieClicked(getMovieFromCursor(cursor, getAdapterPosition()));
        }
    }

    public interface OnFavoriteMovieClickedListener {
        void onFavoriteMovieClicked(MoviePreview movie);
    }
}
