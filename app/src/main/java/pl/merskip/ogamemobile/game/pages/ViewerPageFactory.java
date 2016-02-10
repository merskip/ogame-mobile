package pl.merskip.ogamemobile.game.pages;

import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.buildings.BuildingsListViewer;
import pl.merskip.ogamemobile.game.pages.fleet.FleetShipsFragment;
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
                return new BuildingsListViewer(activity);
            case "fleet":
            case "fleet1":
                return new SimpleFragmentViewer(activity, new FleetShipsFragment());
            default:
                throw new IllegalArgumentException("Unknown pageName: " + pageName);
        }
    }

}
