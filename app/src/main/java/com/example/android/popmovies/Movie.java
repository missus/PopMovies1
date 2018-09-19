/*
 * Created by Karolin Fornet.
 * Copyright (c) 2018.  All rights reserved.
 */

package com.example.android.popmovies;

import java.io.Serializable;

public class Movie implements Serializable {

    private final int mId;
    private final String mTitle;
    private final String mPoster;
    private final String mOverview;
    private final double mRating;
    private final String mReleaseDate;

    Movie(int id, String title, String poster, String overview, double rating, String releaseDate) {
        mId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterUrl() {
        return MainActivity.POSTER_URL + mPoster;
    }
}
