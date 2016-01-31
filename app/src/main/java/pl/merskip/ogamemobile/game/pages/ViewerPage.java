package pl.merskip.ogamemobile.game.pages;

import android.support.v4.app.Fragment;

import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.ResultPage;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Klasa służąca do wyświetlenie strony
 */
public abstract class ViewerPage<Result>{

    protected GameActivity activity;
    protected RequestPage requestPage;
    protected ResultPage resultPage;

    protected ViewerPage(GameActivity activity) {
        this.activity = activity;
    }

    public void setRequestAndResultPage(RequestPage requestPage, ResultPage resultPage) {
        this.requestPage = requestPage;
        this.resultPage = resultPage;
    }

    protected void showFragment(Fragment fragment) {
        String pageName = requestPage.getPageName();
        activity.showContentPage(pageName, fragment);
    }

    public abstract void show(Result result);

}
