package com.blogjihad.nano.p1.moviedb.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.blogjihad.nano.p1.moviedb.MoviesApplication;
import com.blogjihad.nano.p1.moviedb.R;

import com.blogjihad.nano.p1.moviedb.data.api.MovieDbApi;
import com.blogjihad.nano.p1.moviedb.data.contentProvider.FavoriteMoviesContract;
import com.blogjihad.nano.p1.moviedb.data.model.DiscoverMoviesResponse;
import com.blogjihad.nano.p1.moviedb.data.model.Movie;
import com.blogjihad.nano.p1.moviedb.ui.MovieAdapter;
import com.blogjihad.nano.p1.moviedb.ui.home.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A list fragment representing a list of Movies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MovieDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MovieListFragment extends Fragment {
    private static final String TAG = "MovieListFragment";

    @Bind(R.id.listview_movies)
    GridView moviesList;

    private MovieDbApi api;
    private MovieAdapter movieAdapter;
    private Observer<DiscoverMoviesResponse> displayMoviesObserver;
    private Boolean activateFirstItemFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = ((MoviesApplication) getActivity().getApplication()).getApi();
        movieAdapter = new MovieAdapter(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ButterKnife.bind(this, rootView);

        moviesList.setAdapter(movieAdapter);
        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ((MovieListFragment.Callbacks) getActivity()).onItemSelected((Movie) movieAdapter.getItem(i));
            }
        });

        displayMoviesObserver = new Observer<DiscoverMoviesResponse>() {
            @Override
            public void onCompleted() {

                movieAdapter.notifyDataSetChanged();
                moviesList.setDrawSelectorOnTop(true);

                // In two-pane mode, automatically select the first item in the list
                if (activateFirstItemFlag) {
                    moviesList.requestFocusFromTouch();
                    moviesList.setSelection(0);
                    moviesList.performItemClick(movieAdapter.getView(0, null, null), 0, 0);
                }
            }


            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error: " + e);

            }

            @Override
            public void onNext(DiscoverMoviesResponse response) {
                List<Movie> movies = response.getMovies();
                movieAdapter.clear();
                movieAdapter.addAll(movies);
            }
        };


        return rootView;
    }

    private void updateMovies() {

        MoviesApplication app = (MoviesApplication) getActivity().getApplication();
        String sort_by = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_sort_key), "most_popular");

        if (sort_by.equals(getString(R.string.highest_rated_key))) {
        // Highest Rated Movies
            api.getHighestRatedMovies()
                    .flatMap(new Func1<DiscoverMoviesResponse, Observable<DiscoverMoviesResponse>>() {
                        @Override
                        public Observable<DiscoverMoviesResponse> call(DiscoverMoviesResponse discoverMoviesResponse) {
                            return Observable.just(discoverMoviesResponse);
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(displayMoviesObserver);
        } else if (sort_by.equals(getString(R.string.favorites_key))) {
        // Favorite Movies
            Observable<DiscoverMoviesResponse> favoriteMoviesList = getFavoriteMovies();
            favoriteMoviesList.subscribe(displayMoviesObserver);
        } else {
        // Most Popular Movies
            api.getPopularMovies()
                    .flatMap(new Func1<DiscoverMoviesResponse, Observable<DiscoverMoviesResponse>>() {
                        @Override
                        public Observable<DiscoverMoviesResponse> call(DiscoverMoviesResponse discoverMoviesResponse) {

                            return Observable.just(discoverMoviesResponse);
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(displayMoviesObserver);
        }
    }

    private Observable<DiscoverMoviesResponse> getFavoriteMovies() {

        DiscoverMoviesResponse response = new DiscoverMoviesResponse();
        List<Movie> movies = new ArrayList<>();

        Cursor cursor =
                getActivity().getContentResolver().query(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                        null,null,null,null);

        // Populate the movies into the movies list
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Movie favoriteMovie = new Movie();

            favoriteMovie.setTitle(
                    cursor.getString(
                            cursor.getColumnIndex(
                                    FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE)));
            favoriteMovie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    FavoriteMoviesContract.FavoriteMovieEntry._ID))));
            favoriteMovie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(
                    FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE))));
            favoriteMovie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT ))));
            favoriteMovie.setReleaseDate(cursor.getString(cursor.getColumnIndex(
                    FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE )));
            favoriteMovie.setPosterPath(cursor.getString(cursor.getColumnIndex(
                    FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_POSTER_PATH )));
            favoriteMovie.setOverview(cursor.getString(cursor.getColumnIndex(
                    FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW)));

            movies.add(favoriteMovie);
        }

        cursor.close();
        response.setMovies(movies);
        return Observable.just(response);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }


    public void activateFirstItemOnLoad(Boolean activateFlag) {
        activateFirstItemFlag = activateFlag;
    }


    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Movie movie) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

}
