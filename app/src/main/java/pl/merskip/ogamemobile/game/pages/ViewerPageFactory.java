package pl.merskip.ogamemobile.game.pages;

import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.build_items.BuildItemsViewer;
import pl.merskip.ogamemobile.game.pages.overview.OverviewViewer;

/**
 * Fabryka stron
 */
public class ViewerPageFactory {

    private ViewerPageFactory() { }

    public static ViewerPage getViewerPage(GameActivity activity, String pageName) {
        switch (pageName) {
            case "overview":
                return new OverviewViewer(activity);
            case "resources":
            case "station":
            case "research":
            case "shipyard":
            case "defense":
                return new BuildItemsViewer(activity);
            default:
                throw new IllegalArgumentException("Unknown pageName: " + pageName);
        }
    }

}
