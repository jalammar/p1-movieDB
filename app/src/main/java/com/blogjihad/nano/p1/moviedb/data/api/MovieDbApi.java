package com.blogjihad.nano.p1.moviedb.data.api;

import com.blogjihad.nano.p1.moviedb.data.model.DiscoverMoviesResponse;


import retrofit.http.GET;
import rx.Observable;

/**
 * Created by alammr on 10/14/15.
 */
public interface MovieDbApi {

    @GET("/3/discover/movie?sort_by=popularity.desc&api_key=ENTER_API_KEY_HERE")
    Observable<DiscoverMoviesResponse> getPopularMovies();


    @GET("/3/discover/movie?sort_by=vote_average.desc&vote_count.gte=500&api_key=ENTER_API_KEY_HERE")
    Observable<DiscoverMoviesResponse> getHighestRatedMovies();
}
