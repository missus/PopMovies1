/*
 * Created by Karolin Fornet.
 * Copyright (c) 2018.  All rights reserved.
 */

package com.example.android.popmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popmovies.MainActivity.LOG_TAG;

public final class JsonUtils {

    private final static String ID = "id";
    private final static String TITLE = "original_title";
    private final static String POSTER = "poster_path";
    private final static String OVERVIEW = "overview";
    private final static String RATING = "vote_average";
    private final static String RELEASE_DATE = "release_date";

    private JsonUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Movie> extractFeatureFromJson(String moviesJSON) {
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);
            JSONArray moviesArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject currentMovie = moviesArray.getJSONObject(i);
                int id = currentMovie.getInt(ID);
                String title = currentMovie.getString(TITLE);
                String poster = currentMovie.getString(POSTER);
                String overview = currentMovie.getString(OVERVIEW);
                double rating = currentMovie.getDouble(RATING);
                String releaseDate = currentMovie.getString(RELEASE_DATE);

                Movie movie = new Movie(id, title, poster, overview, rating, releaseDate);
                movies.add(movie);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movies JSON results", e);
        }
        return movies;
    }

    public static List<Movie> fetchMovieData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFeatureFromJson(jsonResponse);
    }
}