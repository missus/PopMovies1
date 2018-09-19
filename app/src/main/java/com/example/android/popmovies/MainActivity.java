/*
 * Created by Karolin Fornet.
 * Copyright (c) 2018.  All rights reserved.
 */

package com.example.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popmovies.BuildConfig.API_KEY;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int MOVIE_LOADER_ID = 1;
    private static final String MAIN_URL = "http://api.themoviedb.org/3/movie";
    private static final String URI = "URI";
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private LoaderManager mLoaderManager;
    private String mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Movie> movies = new ArrayList<>();

        mRecyclerView = findViewById(R.id.movie_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns)));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this, movies, new MovieAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), mAdapter.getItem(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            if (savedInstanceState != null && !savedInstanceState.getString(URI).isEmpty()) {
                mUri = savedInstanceState.getString(URI);
                mLoaderManager = getLoaderManager();
                mLoaderManager.initLoader(MOVIE_LOADER_ID, savedInstanceState, MainActivity.this);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        search();
    }

    private void search() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        Uri baseUri = Uri.parse(MAIN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(orderBy);
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        mUri = uriBuilder.toString();
        Bundle search = new Bundle();
        search.putString(URI, mUri);
        mLoaderManager = getLoaderManager();
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, search, this);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(URI, mUri);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, bundle.getString(URI));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mAdapter.clear();
        if (movies != null && !movies.isEmpty()) {
            mAdapter = new MovieAdapter(MainActivity.this, movies, new MovieAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(Movie.class.getSimpleName(), mAdapter.getItem(position));
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

