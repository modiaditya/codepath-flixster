package com.aditya.flixster.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.flixster.R;
import com.aditya.flixster.adapters.MovieListAdapter;
import com.aditya.flixster.helpers.Deserializer;
import com.aditya.flixster.models.Movie;
import com.aditya.flixster.models.MovieCollection;
import com.aditya.flixster.network.TmdbClient;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();

    private MovieListAdapter mMovieListAdapter;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        fetchData(false);
        setSwipeRefreshListener();
        mMovieListAdapter = new MovieListAdapter(this, new ArrayList<Movie>());
        ListView listView = (ListView) findViewById(R.id.movie_list);
        listView.setAdapter(mMovieListAdapter);

    }

    private void fetchData(boolean clearAndFetch) {
        if (clearAndFetch) {
            mMovieListAdapter.clearItems();
            mMovieListAdapter.notifyDataSetChanged();
        }
        TmdbClient.fetchNowShowingMovieData(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Failed to get data from the server. Error " + responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Deserializer deserializer = new Deserializer(MovieCollection.class);
                MovieCollection movieCollection = (MovieCollection) deserializer.parseJSON(responseString);
                Log.e(TAG, movieCollection.movieList.size()+"");
                if (movieCollection.movieList.size() > 0) {
                    mMovieListAdapter.addItems(movieCollection.movieList);
                    mMovieListAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setSwipeRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(true);
            }
        });
    }


}
