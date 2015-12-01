package pl.merskip.ogamemobile.game;

import pl.merskip.ogamemobile.game.pages.overview.GetOverviewTask;
import pl.merskip.ogamemobile.game.pages.research.GetResearchTask;
import pl.merskip.ogamemobile.game.pages.resources.GetResourcesTask;
import pl.merskip.ogamemobile.game.pages.station.GetStationTask;

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
            case "resources":
                return new GetResourcesTask(activity);
            case "station":
                return new GetStationTask(activity);
            case "research":
                return new GetResearchTask(activity);
            default:
                return null;
        }
    }

}
