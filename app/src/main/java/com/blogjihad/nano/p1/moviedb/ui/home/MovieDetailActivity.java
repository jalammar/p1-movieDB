package com.blogjihad.nano.p1.moviedb.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.ShareActionProvider;

import com.blogjihad.nano.p1.moviedb.R;
import com.blogjihad.nano.p1.moviedb.data.model.Movie;
import com.blogjihad.nano.p1.moviedb.ui.settings.SettingsActivity;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link MovieDetailFragment}.
 */
public class MovieDetailActivity extends AppCompatActivity {
    public static final String TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {

            Movie movieData = (Movie) getIntent().getParcelableExtra(MovieDetailFragment.ARG_MOVIE_DATA);

            MovieDetailFragment fragment = MovieDetailFragment.newInstanceFromMovie(movieData);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private ShareActionProvider mShareActionProvider;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);


        // Locate MenuItem with ShareActionProvider
        MenuItem item =  menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider =  (ShareActionProvider)  MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }


    // Call to update the share intent
    public void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

}
