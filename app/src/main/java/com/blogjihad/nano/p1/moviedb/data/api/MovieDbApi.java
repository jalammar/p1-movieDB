package com.blogjihad.nano.p1.moviedb.data.api;

import com.blogjihad.nano.p1.moviedb.data.model.DiscoverMoviesResponse;
import com.blogjihad.nano.p1.moviedb.data.model.MovieVideosResponse;
import com.blogjihad.nano.p1.moviedb.data.model.ReviewsResponse;


import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by alammr on 10/14/15.
 */
public interface MovieDbApi {

    @GET("/3/discover/movie?sort_by=popularity.desc&api_key=ENTER_API_KEY_HERE")
    Observable<DiscoverMoviesResponse> getPopularMovies();


    @GET("/3/discover/movie?sort_by=vote_average.desc&vote_count.gte=500&api_key=ENTER_API_KEY_HERE")
    Observable<DiscoverMoviesResponse> getHighestRatedMovies();


    @GET("/3/movie/{movie_id}/videos?api_key=ENTER_API_KEY_HERE")
    Observable<MovieVideosResponse> getMovieTrailers(@Path("movie_id") Integer movieId);


    @GET("/3/movie/{movie_id}/reviews?api_key=ENTER_API_KEY_HERE")
    Observable<ReviewsResponse> getMovieReviews(@Path("movie_id") Integer movieId);
}
