package com.hackvg.android.mvp.presenters;

import android.text.TextUtils;

import com.hackvg.android.mvp.views.MVPDetailView;
import com.hackvg.common.utils.BusProvider;
import com.hackvg.common.utils.Constants;
import com.hackvg.domain.GetMovieDetailUsecaseController;
import com.hackvg.domain.Usecase;
import com.hackvg.model.entities.MovieDetailResponse;
import com.hackvg.model.entities.Production_companies;
import com.squareup.otto.Subscribe;

import java.util.List;


public class MovieDetailPresenterImpl implements MovieDetailPresenter {

    private final MVPDetailView movieDetailView;
    private final String movieID;


    public MovieDetailPresenterImpl(MVPDetailView movieDetailView, String movieID) {

        this.movieDetailView = movieDetailView;
        this.movieID = movieID;
    }

    @Override
    public void showDescription(String description) {

        movieDetailView.setDescription(description);
    }

    @Override
    public void showCover(String url) {

        String coverUrl = Constants.POSTER_PREFIX + url;
        movieDetailView.setImage(coverUrl);
    }

    @Override
    public void onResume() {

        Usecase getDetailUsecase = new GetMovieDetailUsecaseController(movieID);
        getDetailUsecase.execute();
    }

    @Override
    public void onCreate() {

        BusProvider.getUIBusInstance().register(this);
    }

    @Override
    public void onStop() {

        BusProvider.getUIBusInstance().unregister(this);

    }

    @Override
    public void showTagline(String tagLine) {

        movieDetailView.setTagline(tagLine);
    }

    @Override
    public void showTitle(String title) {

        movieDetailView.setName(title);
    }

    @Override
    public void showCompanies(List<Production_companies> companies) {

        String companiesString = "";

        for (int i = 0; i < companies.size(); i++) {

            Production_companies company = companies.get(i);
            companiesString += company.getName();

            if (i != companies.size() -1)
                companiesString += ", ";
        }

        if (!companies.isEmpty())
            movieDetailView.setCompanies(companiesString);

    }

    @Override
    public void setFavorite() {

    }

    @Subscribe
    @Override
    public void onDetailInformationReceived(MovieDetailResponse response) {

        showDescription(response.getOverview());
        showTitle(response.getTitle());
        showCover(response.getPoster_path());
        showTagline(response.getTagline());
        showCompanies(response.getProduction_companies());
        showHomepage(response.getHomepage());
    }

    @Override
    public void onFavoritePressed() {

    }

    @Override
    public void showHomepage(String homepage) {

        if (!TextUtils.isEmpty(homepage))
            movieDetailView.setHomepage(homepage);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
