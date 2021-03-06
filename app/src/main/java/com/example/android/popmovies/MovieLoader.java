/*
 * Created by Karolin Fornet.
 * Copyright (c) 2018.  All rights reserved.
 */

package com.example.android.popmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private final String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return JsonUtils.fetchMovieData(mUrl);
    }
}