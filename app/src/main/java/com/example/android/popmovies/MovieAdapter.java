/*
 * Created by Karolin Fornet.
 * Copyright (c) 2018.  All rights reserved.
 */

package com.example.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private final LayoutInflater mInflater;
    private final ItemClickListener mClickListener;
    private final Context mContext;

    MovieAdapter(Context context, List<Movie> movies, ItemClickListener clickListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovies = movies;
        this.mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        Picasso.with(mContext).load(movie.getPosterUrl()).into(holder.mPosterImage);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mPosterImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mPosterImage = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition());
        }
    }

    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void clear() {
        mMovies.clear();
        this.notifyDataSetChanged();
    }
}
