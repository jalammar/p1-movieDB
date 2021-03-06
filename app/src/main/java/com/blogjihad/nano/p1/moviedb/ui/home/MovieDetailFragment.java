package com.blogjihad.nano.p1.moviedb.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogjihad.nano.p1.moviedb.MoviesApplication;
import com.blogjihad.nano.p1.moviedb.R;

import com.blogjihad.nano.p1.moviedb.data.api.MovieDbApi;
import com.blogjihad.nano.p1.moviedb.data.contentProvider.FavoriteMoviesContract;
import com.blogjihad.nano.p1.moviedb.data.model.Movie;
import com.blogjihad.nano.p1.moviedb.data.model.MovieVideo;
import com.blogjihad.nano.p1.moviedb.data.model.MovieVideosResponse;
import com.blogjihad.nano.p1.moviedb.data.model.Review;
import com.blogjihad.nano.p1.moviedb.data.model.ReviewsResponse;
import com.blogjihad.nano.p1.moviedb.ui.views.MoviePosterImageView;
import com.blogjihad.nano.p1.moviedb.ui.views.VideoThumbnailImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    public static final String TAG = "MovieDetailFragment";
    public static final String ARG_MOVIE_DATA = "movie_data";


    private MovieDbApi api;

    @Bind(R.id.movie_title)
    TextView title;

    @Bind(R.id.movie_overview)
    TextView overview;

    @Bind(R.id.movie_poster)
    MoviePosterImageView poster;

    @Bind(R.id.movie_rating)
    TextView rating;

    @Bind(R.id.movie_release_date)
    TextView releaseDate;

    @Bind(R.id.movie_rating_vote_count)
    TextView voteCount;


    @Bind(R.id.movie_trailers_label)
    TextView movieTrailerLabel;

    @Bind(R.id.trailers)
    android.support.v7.widget.GridLayout trailersGridLayout;


    @Bind(R.id.movie_reviews_label)
    TextView movieReviewsLabel;

    @Bind(R.id.reviews)
    LinearLayout reviewsLinearLayout;


    @Bind(R.id.fav)
    CheckBox favoriteCheckbox;

    private Movie mMovie;
    public static boolean shareTrailerFlag = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        api = ((MoviesApplication) getActivity().getApplication()).getApi();
        if (getArguments() != null && getArguments().containsKey(ARG_MOVIE_DATA)) {
            mMovie = getArguments().getParcelable(ARG_MOVIE_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        ButterKnife.bind(this, rootView);
        shareTrailerFlag = false;


        if (mMovie != null) {

            title.setText(mMovie.getTitle());
            overview.setText(mMovie.getOverview());
            rating.setText(mMovie.getVoteAverage() + "/10");
            releaseDate.setText(mMovie.getReleaseDate());
            voteCount.setText("(" + mMovie.getVoteCount() + " " + getResources().getString(R.string.votes) + ")");

            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w500" + mMovie.getPosterPath())
                    .into(poster);

            //Check if movie is favorited or not
            boolean isFavorited = false;
            Cursor cursor = getActivity().getContentResolver()
                            .query(FavoriteMoviesContract.FavoriteMovieEntry.buildMoviesUri(mMovie.getId()),
                            null,null,null,null);
            if( cursor.getCount() != 0 ) favoriteCheckbox.setChecked(true);
            cursor.close();

            favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        Uri mNewUri;

                        // Defines an object to contain the new values to insert
                        ContentValues mNewValues = new ContentValues();

                        mNewValues.put(FavoriteMoviesContract.FavoriteMovieEntry._ID,
                                mMovie.getId());
                        mNewValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE,
                                mMovie.getTitle());
                        mNewValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE,
                                mMovie.getVoteAverage());
                        mNewValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT,
                                mMovie.getVoteCount());
                        mNewValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
                                mMovie.getReleaseDate());
                        mNewValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,
                                mMovie.getPosterPath());
                        mNewValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW,
                                mMovie.getOverview());

                        mNewUri = getActivity()
                                .getContentResolver()
                                .insert(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI, mNewValues);
                    }
                    else{
                        getActivity()
                                .getContentResolver()
                                .delete(FavoriteMoviesContract.FavoriteMovieEntry.buildMoviesUri(mMovie.getId()), null, null);
                    }
                }
            });

            //                  TRAILERS
            // Get Movie Trailers (Only trailers specifically. No teasers or featurettes)
            api.getMovieTrailers(mMovie.getId())
                    .flatMap(new Func1<MovieVideosResponse, Observable<MovieVideo>>() {
                        @Override
                        public Observable<MovieVideo> call(MovieVideosResponse movieVideosResponse) {

                            return Observable.from(movieVideosResponse.getVideos());
                        }
                    })
                    .filter(new Func1<MovieVideo, Boolean>() {
                        @Override
                        public Boolean call(MovieVideo video) {
                            return ((String) video.getType()).equals("Trailer");
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieVideo>() {


                        @Override
                        public void onCompleted() {
                            if (trailersGridLayout.getChildCount() > 0) {
                                movieTrailerLabel.setVisibility(View.VISIBLE);
                                trailersGridLayout.setVisibility(View.VISIBLE);
                            }


                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "getMovieTrailers Error: " + e);
                        }
                        @Override
                        public void onNext(MovieVideo video) {
                            FrameLayout imageLayoutView = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.list_item_trailer, trailersGridLayout, false);
                            VideoThumbnailImageView imageView = (VideoThumbnailImageView) imageLayoutView.findViewById(R.id.thumbnail_image);
                            Picasso.with(getActivity())
                                    .load("https://img.youtube.com/vi/" + video.getKey() + "/0.jpg")
                                    .into(imageView);
                            imageLayoutView.setTag(video.getKey());

                            imageLayoutView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String key = (String) view.getTag();
                                    String url = "https://www.youtube.com/watch?v=" + key;
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });


                            // Set the intent of the SHARE button in the toolbar
                            // Only set the first trailer, though
                            if( !shareTrailerFlag ){
                                String key = video.getKey();
                                String url = "https://www.youtube.com/watch?v=" + key;
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.putExtra(Intent.EXTRA_TEXT, url);
                                i.setType("text/plain");
                                ((MovieDetailActivity) getActivity()).setShareIntent(i);
                                shareTrailerFlag = true;
                            }
                            trailersGridLayout.addView(imageLayoutView);


                        }
                    });


            //                  Reviews
            api.getMovieReviews(mMovie.getId())
                    .flatMap(new Func1<ReviewsResponse, Observable<Review>>() {
                        @Override
                        public Observable<Review> call(ReviewsResponse reviewsResponse) {

                            return Observable.from(reviewsResponse.getReviews());
                        }
                    })
                    .take(3)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Review>() {

                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "getMovieReviews onCompleted ");

                            if (reviewsLinearLayout.getChildCount() > 0) {
                                movieReviewsLabel.setVisibility(View.VISIBLE);
                                reviewsLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "getMovieReviews Error: " + e);
                        }

                        @Override
                        public void onNext(Review review) {
                            Log.d(TAG, "getMovieReviews onNext ");
                            LinearLayout reviewLayoutView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.list_item_review, null);
                            TextView reviewAuthor = (TextView) reviewLayoutView.findViewById(R.id.review_author);
                            reviewAuthor.setText(review.getAuthor());

                            TextView reviewText = (TextView) reviewLayoutView.findViewById(R.id.review_text);
                            reviewText.setText(review.getContent());

                            reviewsLinearLayout.addView(reviewLayoutView);
                        }
                    });
        }

        return rootView;
    }


    public static MovieDetailFragment newInstanceFromMovie(Movie movie) {
        MovieDetailFragment f = new MovieDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE_DATA, movie);
        f.setArguments(args);

        return f;
    }
}
