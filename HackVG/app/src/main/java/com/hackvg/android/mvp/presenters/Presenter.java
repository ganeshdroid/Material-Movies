
/**
 * @author Saul M.
 */
package com.hackvg.android.mvp.presenters;


/**
 * Interface that represents a Presenter in the model view presenter Pattern
 */
public interface Presenter {

    /**
     * Method controlled by the lifecycle of the view that is implemented by
     * an Activity or Fragment
     */
    public void start ();

    /**
     * Method controlled by the lifecycle of the view that is implemented by
     * an Activity or Fragment
     */
    public void stop ();
}
