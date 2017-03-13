package com.aditya.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.flixster.R;
import com.aditya.flixster.activities.MovieDetailActivity;
import com.aditya.flixster.activities.YoutubePlayerActivity;
import com.aditya.flixster.models.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amodi on 3/9/17.
 */

public class MovieListAdapter extends ArrayAdapter<Movie> {

    private static final int MOVIE_ITEM_ROW_TYPE = 0;
    private static final int POPULAR_MOVIE_ITEM_ROW_TYPE = 1;

    private final Context mContext;
    private final ArrayList<Movie> mMovies;
    private final boolean isPortrait;

    public MovieListAdapter(@NonNull Context context,
                            ArrayList<Movie> movies) {
        super(context, 0, movies);
        mContext = context;
        mMovies = movies;
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPortrait = false;
        } else {
            isPortrait = true;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPortrait) {
            Movie movie = mMovies.get(position);
            if (movie.voteAverage <= 7) {
                return MOVIE_ITEM_ROW_TYPE;
            } else {
                return POPULAR_MOVIE_ITEM_ROW_TYPE;
            }
        } else {
            return MOVIE_ITEM_ROW_TYPE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return isPortrait ? 2 : 1;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = mMovies.get(position);
        int rowType = getItemViewType(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            if (isPortrait) {
                if (rowType == MOVIE_ITEM_ROW_TYPE) {
                    convertView = layoutInflater.inflate(R.layout.movie_item, parent, false);
                    viewHolder = new MovieItemViewHolder(convertView);

                } else {
                    convertView = layoutInflater.inflate(R.layout.popular_movie_item, parent, false);
                    viewHolder = new PopularMovieItemViewHolder(convertView);
                }
            } else {
                convertView = layoutInflater.inflate(R.layout.movie_item, parent, false);
                viewHolder = new MovieItemViewHolder(convertView);
            }
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.initMovie(movie);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, MovieDetailActivity.class);
                i.putExtra(MovieDetailActivity.MOVIE_EXTRA, mMovies.get(position));
                mContext.startActivity(i);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    public void addItems(List<Movie> movieList) {
        mMovies.addAll(movieList);
    }

    public void clearItems() {
        mMovies.clear();
    }


    private interface ViewHolder {
        void initMovie(Movie movie);
    }
    public class PopularMovieItemViewHolder implements ViewHolder {
        @BindView(R.id.backgrop_image) ImageView mBackgropImage;
        @BindView(R.id.play_button) ImageView mPlayButtonImage;
        @BindView(R.id.popular_movie_title_text) TextView mMovieTitle;

        public PopularMovieItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Override
        public void initMovie(final Movie movie) {
            mMovieTitle.setText(movie.title);
            Picasso.with(mContext).load(movie.getBackdropImageUrl())
                   .transform(new RoundedCornersTransformation(10, 10))
                   .placeholder(R.drawable.ic_video)
                   .into(
                        mBackgropImage,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                mMovieTitle.setVisibility(View.VISIBLE);
                                mPlayButtonImage.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            mPlayButtonImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, YoutubePlayerActivity.class);
                    i.putExtra(YoutubePlayerActivity.MOVIE_ID_EXTRA, movie.id);
                    mContext.startActivity(i);
                }
            });

        }
    }

    public class MovieItemViewHolder implements ViewHolder {
        @BindView(R.id.title_text) TextView mTitle;
        @BindView(R.id.overview_text) TextView mOverview;
        @BindView(R.id.poster_image) ImageView mImageView;

        public MovieItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Override
        public void initMovie(final Movie movie) {
            mTitle.setText(movie.title);
            mOverview.setText(movie.overview);
            if (isPortrait) {
                Picasso.with(mContext).load(movie.getPosterImageUrl())
                       .transform(new RoundedCornersTransformation(10, 10))
                       .placeholder(R.drawable.ic_video)
                       .into(mImageView);
            } else {
                Picasso.with(mContext).load(movie.getBackdropImageUrl())
                       .transform(new RoundedCornersTransformation(10, 10))
                       .placeholder(R.drawable.ic_video)
                       .into(mImageView);
            }

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, YoutubePlayerActivity.class);
                    i.putExtra(YoutubePlayerActivity.MOVIE_ID_EXTRA, movie.id);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
