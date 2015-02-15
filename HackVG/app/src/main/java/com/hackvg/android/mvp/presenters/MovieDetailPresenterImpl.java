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

    private final MVPDetailView mMovieDetailView;
    private final String mMovieID;


    public MovieDetailPresenterImpl(MVPDetailView movieDetailView, String movieID) {

        mMovieDetailView = movieDetailView;
        mMovieID = movieID;
    }

    @Override
    public void showDescription(String description) {

        mMovieDetailView.setDescription(description);
    }

    @Override
    public void showCover(String url) {

        String coverUrl = Constants.BASIC_STATIC_URL + url;
        mMovieDetailView.setImage(coverUrl);
    }

    @Override
    public void start() {

        BusProvider.getUIBusInstance().register(this);

        Usecase getDetailUsecase = new GetMovieDetailUsecaseController(mMovieID);
        getDetailUsecase.execute();
    }

    @Override
    public void stop() {

        BusProvider.getUIBusInstance().unregister(this);
    }

    @Override
    public void showTagline(String tagLine) {

        mMovieDetailView.setTagline(tagLine);
    }

    @Override
    public void showTitle(String title) {

        mMovieDetailView.setName(title);
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
            mMovieDetailView.setCompanies(companiesString);
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
    public void onFavoritePressed() {}

    @Override
    public void showHomepage(String homepage) {

        if (!TextUtils.isEmpty(homepage))
            mMovieDetailView.setHomepage(homepage);
    }
}
