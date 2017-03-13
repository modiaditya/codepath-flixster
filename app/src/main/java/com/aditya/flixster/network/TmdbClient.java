package com.aditya.flixster.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by amodi on 3/9/17.
 */

public class TmdbClient {

    private static final String TAG = TmdbClient.class.getSimpleName();
    private static final String API_KEY = "api_key";
    private static final String API_KEY_VALUE = "79da8149bd96c7bdc45fd2a3ee4ffb1a";

    private static final AsyncHttpClient sAsyncHttpClient;

    static {
        sAsyncHttpClient = new AsyncHttpClient();
    }

    public static void fetchNowShowingMovieData(TextHttpResponseHandler textHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put(API_KEY, API_KEY_VALUE);
        sAsyncHttpClient.get("https://api.themoviedb.org/3/movie/now_playing", params, textHttpResponseHandler);
    }

    public static void fetchTrailersData(String movieId, JsonHttpResponseHandler jsonHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put(API_KEY, API_KEY_VALUE);
        sAsyncHttpClient.get(String.format("https://api.themoviedb.org/3/movie/%s/videos", movieId), params, jsonHttpResponseHandler);
    }

    public static void fetchGenreData(TextHttpResponseHandler textHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put(API_KEY, API_KEY_VALUE);
        params.put("language", "en-US");

        sAsyncHttpClient.get("https://api.themoviedb.org/3/genre/movie/list", params, textHttpResponseHandler);
    }

    public static void fetchMovieDetails(String movieId, TextHttpResponseHandler textHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put(API_KEY, API_KEY_VALUE);
        params.put("language", "en-US");
        sAsyncHttpClient.get(String.format("https://api.themoviedb.org/3/movie/%s", movieId), params, textHttpResponseHandler);
    }

}
