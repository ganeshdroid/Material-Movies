package com.hackvg.model.rest;


import com.hackvg.common.utils.BusProvider;
import com.hackvg.common.utils.Constants;
import com.hackvg.model.MediaDataSource;
import com.hackvg.model.entities.MovieDetailResponse;
import com.hackvg.model.entities.PopularMoviesApiResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by saulmm on 31/01/15.
 */
public class RestMovieSource implements MediaDataSource {

    public static RestMovieSource INSTANCE;
    private final MovieDatabaseAPI moviesDBApi;

    private RestMovieSource() {

        RestAdapter parkappRest = new RestAdapter.Builder()
            .setEndpoint(Constants.MOVIE_DB_HOST)
            .build();

        moviesDBApi = parkappRest.create(MovieDatabaseAPI.class);
    }

    public static RestMovieSource getInstance() {

        if (INSTANCE == null)
            INSTANCE = new RestMovieSource();

        return INSTANCE;
    }

    @Override
    public void getShows() {

//        moviesDBApi.getPopularShows(Constants.API_KEY, moviesResponseCallback);
    }


    @Override
    public void getMovies() {

        moviesDBApi.getPopularMovies(Constants.API_KEY, retrofitCallback);
    }

    public Callback retrofitCallback = new Callback() {
        @Override
        public void success(Object o, Response response) {


            if (o instanceof MovieDetailResponse) {

                MovieDetailResponse detailResponse = (MovieDetailResponse) o;
                BusProvider.getRestBusInstance().post(detailResponse);

            } else if (o instanceof PopularMoviesApiResponse) {

                PopularMoviesApiResponse moviesApiResponse = (PopularMoviesApiResponse) o;

                BusProvider.getRestBusInstance().post(
                    moviesApiResponse);
            }
        }

        @Override
        public void failure(RetrofitError error) {

            System.out.printf("[DEBUG] RestMovieSource failure - "+error.getMessage());
        }
    };

    @Override
    public void getDetailMovie(String id) {

        moviesDBApi.getMovieDetail(Constants.API_KEY, id, retrofitCallback);
    }
}
