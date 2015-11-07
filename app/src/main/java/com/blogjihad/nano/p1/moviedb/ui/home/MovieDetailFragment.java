package com.blogjihad.nano.p1.moviedb.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogjihad.nano.p1.moviedb.R;

import com.blogjihad.nano.p1.moviedb.data.model.Movie;
import com.blogjihad.nano.p1.moviedb.ui.MoviePosterImageView;
import com.blogjihad.nano.p1.moviedb.ui.home.dummy.DummyContent;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    public static final String TAG = "MovieDetailFragment";

    public static final String ARG_MOVIE_DATA = "movie_data";

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

    private Movie mMovie;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        if (mMovie != null) {

            title.setText(mMovie.getTitle());
            overview.setText(mMovie.getOverview());
            rating.setText(mMovie.getVoteAverage() + "/10");
            releaseDate.setText(mMovie.getReleaseDate());
            voteCount.setText("(" + mMovie.getVoteCount() + " " + getResources().getString(R.string.votes) + ")");

            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w500" + mMovie.getPosterPath())
                    .into(poster);
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
