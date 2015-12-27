package com.blogjihad.nano.p1.moviedb;

import android.app.Application;
import android.content.Context;

import com.blogjihad.nano.p1.moviedb.data.LoggingInterceptor;
import com.blogjihad.nano.p1.moviedb.data.api.MovieDbApi;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

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


        final Context context = this;
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(new SampleDumperPluginsProvider(context))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                        .build());
    }

    private static class SampleDumperPluginsProvider implements DumperPluginsProvider {
        private final Context mContext;

        public SampleDumperPluginsProvider(Context context) {
            mContext = context;
        }

        @Override
        public Iterable<DumperPlugin> get() {
            ArrayList<DumperPlugin> plugins = new ArrayList<DumperPlugin>();
            for (DumperPlugin defaultPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()) {
                plugins.add(defaultPlugin);
            }
//            plugins.add(new HelloWorldDumperPlugin());
            return plugins;
        }
//
//        final Context context = this;
//        Stetho.initialize(Stetho.newInitializerBuilder(context)
//                .enableDumpapp(new DumperPluginsProvider() {
//                    @Override
//                    public Iterable<DumperPlugin> get() {
//                        return new Stetho.DefaultDumperPluginsBuilder(context)
//                                .provide(new MyDumperPlugin())
//                                .finish();
//                    }
//                })
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
//                .build());

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
