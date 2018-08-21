package com.example.android.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Movie Movie = (Movie) getIntent().getSerializableExtra(Movie.class.getSimpleName());
        ImageView PosterImage = findViewById(R.id.poster);
        TextView Title = findViewById(R.id.title);
        TextView Rating = findViewById(R.id.rating);
        TextView ReleaseDate = findViewById(R.id.release_date);
        TextView Overview = findViewById(R.id.overview);

        Title.setText(Movie.getTitle());
        Rating.setText(Movie.getRating() + "/10");
        ReleaseDate.setText(Movie.getReleaseDate());
        Overview.setText(Movie.getOverview());
        Picasso.with(this).load(Movie.getPosterUrl()).into(PosterImage);

    }
}
