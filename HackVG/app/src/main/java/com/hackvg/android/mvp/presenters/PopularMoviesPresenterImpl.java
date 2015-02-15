package com.hackvg.android.mvp.presenters;

import com.hackvg.android.mvp.views.MVPPopularMoviesView;
import com.hackvg.common.utils.BusProvider;
import com.hackvg.common.utils.Constants;
import com.hackvg.domain.ConfigurationUsecaseController;
import com.hackvg.domain.GetMoviesUsecase;
import com.hackvg.domain.GetMoviesUsecaseController;
import com.hackvg.domain.Usecase;
import com.hackvg.model.entities.PopularMoviesApiResponse;
import com.squareup.otto.Subscribe;


public class PopularMoviesPresenterImpl implements PopularMoviesPresenter {

    private final MVPPopularMoviesView MVPPopularMoviesView;

    public PopularMoviesPresenterImpl(MVPPopularMoviesView MVPPopularMoviesView) {

        this.MVPPopularMoviesView = MVPPopularMoviesView;
    }

    @Override
    public void onCreate() {

        BusProvider.getUIBusInstance().register(this);

        MVPPopularMoviesView.showLoading();

        Usecase configureUsecase = new ConfigurationUsecaseController();
        configureUsecase.execute();
    }

    @Override
    public void onStop() {

        BusProvider.getUIBusInstance().unregister(this);
    }


    @Subscribe
    @Override
    public void onPopularMoviesReceived(PopularMoviesApiResponse popularMovies) {

        MVPPopularMoviesView.hideLoading();
        MVPPopularMoviesView.showMovies(popularMovies.getResults());
    }

    @Subscribe
    @Override
    public void onConfigurationFinished (String baseImageUrl) {

        Constants.BASIC_STATIC_URL = baseImageUrl;

        Usecase getPopularShows = new GetMoviesUsecaseController(GetMoviesUsecase.TV_MOVIES);
        getPopularShows.execute();
    }
}
