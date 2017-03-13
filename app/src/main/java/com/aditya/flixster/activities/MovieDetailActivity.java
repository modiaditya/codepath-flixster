package com.aditya.flixster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.flixster.R;
import com.aditya.flixster.helpers.Deserializer;
import com.aditya.flixster.models.Genre;
import com.aditya.flixster.models.Movie;
import com.aditya.flixster.models.MovieDetails;
import com.aditya.flixster.network.TmdbClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import java.util.List;

/**
 * Created by amodi on 3/12/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String MOVIE_EXTRA = "movie";

    @BindView(R.id.backgrop_image) ImageView mBackdropImage;
    @BindView(R.id.poster_image) ImageView mPosterImage;
    @BindView(R.id.movie_title_text) TextView mTitleTextView;
    @BindView(R.id.genre_text) TextView mGenreTextView;
    @BindView(R.id.running_time) TextView mRunningTimeTextView;
    @BindView(R.id.release_date_text) TextView mReleaseDateTextView;
    @BindView(R.id.vote_text) TextView mVoteTextView;
    @BindView(R.id.overview_text) TextView mOverviewTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);


        final Movie movie = (Movie) getIntent().getSerializableExtra(MOVIE_EXTRA);

        TmdbClient.fetchMovieDetails(String.valueOf(movie.id), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Failure while getting movie details data", throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Deserializer deserializer = new Deserializer(MovieDetails.class);
                MovieDetails movieDetails = (MovieDetails) deserializer.parseJSON(responseString);
                mGenreTextView.setText(getGenre(movieDetails.genres));
                mRunningTimeTextView.setText(getRunningTime(movieDetails.runtime));
            }
        });

        mTitleTextView.setText(movie.title);
        mVoteTextView.setText(getRating(movie.voteAverage));
        mOverviewTextView.setText(movie.overview);
        mReleaseDateTextView.setText(getReleaseDate(movie.releaseDate));
        Picasso.with(this).load(movie.getBackdropImageUrl())
               .transform(new RoundedCornersTransformation(10, 10))
               .placeholder(R.drawable.ic_video)
               .into(mBackdropImage);
        Picasso.with(this).load(movie.getPosterImageUrl())
               .transform(new RoundedCornersTransformation(10, 10))
               .placeholder(R.drawable.ic_video)
               .into(mPosterImage);
        mBackdropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieDetailActivity.this, YoutubePlayerActivity.class);
                i.putExtra(YoutubePlayerActivity.MOVIE_ID_EXTRA, movie.id);
                MovieDetailActivity.this.startActivity(i);
            }
        });

    }

    private String getRating(double voteAverage) {
        return voteAverage + "/10";
    }

    private String getReleaseDate(String date) {
        return date.split("-")[0];
    }

    private String getGenre(List<Genre> genres) {
        String genre = "";
        int i;
        for (i = 0; i < genres.size()-1; i++) {
            genre = genre.concat(genres.get(i).name + ", ");
        }
        if (genres.size() > 0) {
            genre = genre.concat(genres.get(i).name);
        }
        return genre;
    }

    private String getRunningTime(Integer runnningTime) {
        int hours = runnningTime / 60;
        int minutes = runnningTime % 60;
        if (hours > 1) {
            return String.format("%d hrs %02d mins", hours, minutes);
        } else {
            return String.format("%d hr %02d mins", hours, minutes);
        }
    }
}
