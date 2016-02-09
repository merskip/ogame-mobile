package pl.merskip.ogamemobile.adapter.game.pages;

import pl.merskip.ogamemobile.adapter.game.ResultPage;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsResult;

/**
 * Fabryka stron
 */
public class ResultPageFactory {

    private ResultPageFactory() { }

    public static ResultPage getResultPage(String pageName) {
        switch (pageName) {
            case "overview":
                return new OverviewResult();
            case "resources":
                return new ResourcesResult();
            case "station":
                return new StationResult();
            case "research":
                return new ResearchResult();
            case "shipyard":
                return new ShipyardResult();
            case "defense":
                return new DefenseResult();
            case "fleet":
            case "fleet1":
                return new FleetShipsResult();
            default:
                throw new IllegalArgumentException("Unknown pageName: " + pageName);
        }
    }

}
