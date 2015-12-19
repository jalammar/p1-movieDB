package com.blogjihad.nano.p1.moviedb.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.blogjihad.nano.p1.moviedb.R;
import com.blogjihad.nano.p1.moviedb.data.model.Movie;
import com.blogjihad.nano.p1.moviedb.ui.views.MoviePosterImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context context;


    public MovieAdapter(Activity context) {
        super(context, 0);
        this.context = context;

    }

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Movie movie = getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Picasso.with(this.context)
                .load("http://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(holder.movieImage);

        return convertView;
    }


    static class ViewHolder {


        @Bind(R.id.list_item_movie_image)
        MoviePosterImageView movieImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
