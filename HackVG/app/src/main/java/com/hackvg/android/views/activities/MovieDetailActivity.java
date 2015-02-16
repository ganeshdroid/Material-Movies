package com.hackvg.android.views.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackvg.android.R;
import com.hackvg.android.mvp.presenters.MovieDetailPresenter;
import com.hackvg.android.mvp.views.MVPDetailView;
import com.hackvg.android.utils.GUIUtils;
import com.hackvg.android.utils.HackVGTransitionListener;
import com.hackvg.android.views.custom_views.ObservableScrollView;
import com.hackvg.android.views.custom_views.ScrollViewListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class MovieDetailActivity extends Activity implements MVPDetailView,
    Palette.PaletteAsyncListener, ScrollViewListener {

    @InjectViews({
        R.id.activity_detail_title,
        R.id.activity_detail_content,
        R.id.activity_detail_homepage,
        R.id.activity_detail_company,
        R.id.activity_detail_tagline,
        R.id.activity_detail_confirmation_text,
    }) List<TextView> movieInfoTextViews;

    @InjectViews({
        R.id.activity_detail_header_tagline,
        R.id.activity_detail_header_description
    }) List<TextView> headers;

    @InjectView(R.id.activity_detail_book_info)              View overviewContainer;
    @InjectView(R.id.activity_detail_fab)                    ImageView fabButton;
    @InjectView(R.id.activity_detail_cover)                  ImageView coverImageView;
    @InjectView(R.id.activity_detail_confirmation_image)     ImageView confirmationView;
    @InjectView(R.id.activity_detail_confirmation_container) FrameLayout confirmationContainer;

    @InjectView(R.id.activity_movie_detail_scroll)           ObservableScrollView observableScrollView;

    private final int TITLE         = 0;
    private final int DESCRIPTION   = 1;
    private final int HOMEPAGE      = 2;
    private final int COMPANY       = 3;
    private final int TAGLINE       = 4;
    private final int CONFIRMATION  = 5;

    private MovieDetailPresenter detailPresenter;
    private Palette.Swatch mBrightSwatch;
    private Drawable fabRipple;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().getSharedElementEnterTransition().addListener(transitionListener);
        GUIUtils.makeTheStatusbarTranslucent(this);
        ButterKnife.inject(this);

        int moviePosition = getIntent().getIntExtra("movie_position", 0);
        String movieID = getIntent().getStringExtra("movie_id");
        coverImageView.setTransitionName("cover" + moviePosition);

        fabRipple = getResources().getDrawable(R.drawable.ripple_round);
        fabButton.setBackground(fabRipple);

        observableScrollView.setScrollViewListener(this);

        detailPresenter = new MovieDetailPresenter(this, movieID);
    }

    @Override
    protected void onStop() {

        super.onStop();
        detailPresenter.stop();
    }

    @Override
    protected void onStart() {

        super.onStart();
        detailPresenter.start();
    }

    @Override
    public void setImage(String url) {

        Bitmap bookCoverBitmap = MoviesActivity.sPhotoCache.get(0);
        coverImageView.setBackground(new BitmapDrawable(getResources(), bookCoverBitmap));

        Palette.generateAsync(bookCoverBitmap, this);
    }

    @Override
    public void setName(String title) {

        movieInfoTextViews.get(TITLE).setText(title);
    }

    @Override
    public void setDescription(String description) {

        movieInfoTextViews.get(DESCRIPTION).setText(description);
    }

    @Override
    public Context getContext() {

        return this;
    }

    @Override
    public void finish(String cause) {

        Toast.makeText(this, cause, Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void showConfirmationView() {

        GUIUtils.showViewByRevealEffect(confirmationContainer,
            fabButton, GUIUtils.getWindowWidth(this));

        animateConfirmationView();
        startClosingConfirmationView();
    }

    @Override
    public void animateConfirmationView() {

        Drawable drawable = confirmationView.getDrawable();

        if (drawable instanceof Animatable)
            ((Animatable) drawable).start();
    }

    @Override
    public void startClosingConfirmationView() {

        int milliseconds = 1500;

        getWindow().setReturnTransition(new Slide());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                observableScrollView.setVisibility(View.GONE);
                MovieDetailActivity.this. finishAfterTransition();
            }

        }, milliseconds);
    }

    @Override
    public void showFabButton() {

        boolean showFab = true;
    }

    @Override
    public void setHomepage(String homepage) {

        movieInfoTextViews.get(HOMEPAGE).setVisibility(View.VISIBLE);
        movieInfoTextViews.get(HOMEPAGE).setText(homepage);
    }

    @Override
    public void setCompanies(String companies) {

        movieInfoTextViews.get(COMPANY).setVisibility(View.VISIBLE);
        movieInfoTextViews.get(COMPANY).setText(companies);
    }

    @Override
    public void setTagline(String tagline) {

        movieInfoTextViews.get(TAGLINE).setText(tagline);
    }

    @Override
    public void onGenerated(Palette palette) {

        if (palette != null) {

            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
            Palette.Swatch lightSwatch = palette.getLightVibrantSwatch();

            if (lightSwatch != null) {

                overviewContainer.setBackgroundColor(lightSwatch.getRgb());

                ButterKnife.apply(movieInfoTextViews, GUIUtils.setter, lightSwatch.getTitleTextColor());

                fabRipple.setColorFilter(lightSwatch.getRgb(), PorterDuff.Mode.ADD);
                confirmationContainer.setBackgroundColor(lightSwatch.getRgb());

            } else {

                int primaryColor = getResources()
                    .getColor(R.color.theme_primary);

                int accentColor = getResources()
                    .getColor(R.color.theme_accent);

                fabRipple.setColorFilter(accentColor, PorterDuff.Mode.ADD);
                confirmationView.setBackgroundColor(primaryColor);
                overviewContainer.setBackgroundColor(primaryColor);
            }

            if (lightSwatch == null && vibrantSwatch != null)
                colorBrightElements(vibrantSwatch);

            if (darkVibrantSwatch != null && lightSwatch != null)
                colorBrightElements(darkVibrantSwatch);
        }
    }

    public void colorBrightElements (Palette.Swatch brightSwatch) {

        Drawable drawable = confirmationView.getDrawable();
        drawable.setColorFilter(brightSwatch.getRgb(), PorterDuff.Mode.MULTIPLY);

        movieInfoTextViews.get(CONFIRMATION).setTextColor(brightSwatch.getRgb());
        movieInfoTextViews.get(TITLE).setTextColor(brightSwatch.getTitleTextColor());
        movieInfoTextViews.get(TITLE).setBackgroundColor(brightSwatch.getRgb());

        mBrightSwatch = brightSwatch;

        getWindow().setNavigationBarColor(mBrightSwatch.getRgb());

        if (brightSwatch != null) {

            if (movieInfoTextViews.get(HOMEPAGE).getVisibility() == View.VISIBLE)
                GUIUtils.tintAndSetCompoundDrawable(this, R.drawable.ic_domain_white_24dp,
                    brightSwatch.getRgb(), movieInfoTextViews.get(HOMEPAGE));

            if (movieInfoTextViews.get(COMPANY).getVisibility() == View.VISIBLE)
                GUIUtils.tintAndSetCompoundDrawable(this, R.drawable.ic_public_white_24dp,
                    brightSwatch.getRgb(), movieInfoTextViews.get(COMPANY));

            ButterKnife.apply(headers, GUIUtils.setter, brightSwatch.getRgb());
        }
    }

    @OnClick(R.id.activity_detail_fab)
    public void onClick() {

        showConfirmationView();
    }

    boolean isTranslucent = false;

    @Override
    public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {

        if (y > coverImageView.getHeight()) {

            movieInfoTextViews.get(TITLE).setTranslationY(y - coverImageView.getHeight());

            if (!isTranslucent) {
                GUIUtils.setTheStatusbarNotTranslucent(this);
                getWindow().setStatusBarColor(mBrightSwatch.getRgb());
                isTranslucent = true;
            }
        }

        if (y < coverImageView.getHeight() && isTranslucent) {

            GUIUtils.makeTheStatusbarTranslucent(this);
            movieInfoTextViews.get(TITLE).setTranslationY(0);
            isTranslucent = false;
        }
    }

    private final HackVGTransitionListener transitionListener = new HackVGTransitionListener() {

        @Override
        public void onTransitionEnd(Transition transition) {

        super.onTransitionEnd(transition);
        GUIUtils.showViewByScale(fabButton);
        }
    };
}
