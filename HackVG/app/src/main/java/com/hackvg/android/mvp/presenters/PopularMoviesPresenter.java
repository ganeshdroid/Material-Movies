package com.hackvg.android.mvp.presenters;

import com.hackvg.model.entities.PopularMoviesApiResponse;

/**
 * Created by saulmm on 31/01/15.
 */
public interface PopularMoviesPresenter {

    public void onCreate ();

    public void onStop ();

    public void onPopularMoviesReceived(PopularMoviesApiResponse popularMovies);

    public void onConfigurationFinished (String baseImageUrl);
}
