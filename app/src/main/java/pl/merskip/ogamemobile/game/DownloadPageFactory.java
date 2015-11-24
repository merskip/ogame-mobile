package pl.merskip.ogamemobile.game;

import pl.merskip.ogamemobile.game.pages.overview.GetOverviewTask;

/**
 * Fabryka asynchronicznego pobierania stron
 */
public class DownloadPageFactory {

    private GameActivity activity;

    public DownloadPageFactory(GameActivity activity) {
        this.activity = activity;
    }

    public DownloadPageTask<?> getDownloadPageTask(String pageName) {
        return getDownloadPageTask(activity, pageName);
    }

    public static DownloadPageTask<?> getDownloadPageTask(GameActivity activity, String pageName) {
        switch (pageName) {
            case "overview":
                return new GetOverviewTask(activity);
            default:
                return null;
        }
    }

}