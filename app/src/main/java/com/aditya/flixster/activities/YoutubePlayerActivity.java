package com.aditya.flixster.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.flixster.R;
import com.aditya.flixster.network.TmdbClient;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by amodi on 3/12/17.
 */

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    private static final String TAG = YoutubePlayerActivity.class.getSimpleName();
    private static final String YOUTUBE_API_KEY = "AIzaSyCc0FheEbDXJo9Oil2jDEIKS_SOeQq2lo4";
    public static final String MOVIE_ID_EXTRA = "movie_id_extra";

    @BindView(R.id.player) YouTubePlayerView mYoutubePlayer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.youtube_player);
        ButterKnife.bind(this);
        final String movieId = String.valueOf(getIntent().getLongExtra(MOVIE_ID_EXTRA, 0));

        mYoutubePlayer.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,
                                                boolean b) {

                fetchTrailerDataAndPlay(movieId, youTubePlayer);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(TAG, "Error initalizing youtube player " + youTubeInitializationResult.name());
            }
        });
    }

    private void fetchTrailerDataAndPlay(String movieId, final YouTubePlayer youTubePlayer) {
        TmdbClient.fetchTrailersData(movieId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    if (results.length() == 0) {
                        return;
                    }
                    String trailerKey = results.getJSONObject(0).getString("key");
                    youTubePlayer.loadVideo(trailerKey);
                } catch (JSONException e) {
                    Log.e(TAG, "Error while fetching data", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, "Error while fetching data from Tmdb ", throwable);
                Toast.makeText(YoutubePlayerActivity.this, "Cannot play video", LENGTH_SHORT).show();
            }
        });
    }
}
