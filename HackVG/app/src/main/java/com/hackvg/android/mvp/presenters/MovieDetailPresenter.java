
/**
 * @author Saul M.
 */
package com.hackvg.android.mvp.presenters;

import com.hackvg.model.entities.MovieDetailResponse;
import com.hackvg.model.entities.Production_companies;

import java.util.List;

/**
 * {@link Presenter} that controls communication between the MovieDetail of the presentation
 * layer and the Usecases in the domain layer
 */
@SuppressWarnings("UnusedDeclaration")
public interface MovieDetailPresenter extends Presenter  {

    /**
     * Shows the description on the View
     * @param description of the film
     */
    public void showDescription (String description);

    /**
     * Shows an image on the view
     * @param url resource of the image
     */
    public void showCover (String url);

    public void onCreate ();

    public void onStop ();

    /**
     * Shows the tagline on the View
     * @param tagLine of the film
     */
    public void showTagline (String tagLine);

    /**
     * Shows the title on the View
     * @param title of the film
     */
    public void showTitle(String title);

    /**
     * Show the production companies on te View
     * @param companies a list with all companies that participate in the production
     * of the film
     */
    public void showCompanies (List<Production_companies> companies);

    /**
     * Called by {@link com.hackvg.domain.GetMovieDetailUsecaseController} when the
     * detail information is fetched from the data source
     *
     * @param response a container including the data of the film
     */
    public void onDetailInformationReceived (MovieDetailResponse response);

    /**
     * Sets the film as favorite, fired from the View
     */
    public void onFavoritePressed ();

    /**
     * Sets the homepage on the View
     *
     * @param homepage the main webpage of the film
     */
    public void showHomepage (String homepage);
}
