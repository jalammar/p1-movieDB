package com.blogjihad.nano.p1.moviedb;

import android.app.Application;

import com.blogjihad.nano.p1.moviedb.data.LoggingInterceptor;
import com.blogjihad.nano.p1.moviedb.data.api.MovieDbApi;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by alammr on 10/23/15.
 */
public class MoviesApplication extends Application {

    private MovieDbApi api;

    @Override
    public void onCreate() {
        super.onCreate();

        initMovieApi();
    }

    private void initMovieApi(){
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());

        String api_url = "http://api.themoviedb.org";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        //.setLogLevel(Retrofit.LogLevel.FULL)
                .client(client)
                .build();

        api = retrofit.create(MovieDbApi.class);
    }

    public MovieDbApi getApi(){
        return api;
    }
}
