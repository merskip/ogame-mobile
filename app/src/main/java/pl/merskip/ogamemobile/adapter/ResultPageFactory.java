package pl.merskip.ogamemobile.adapter;

import pl.merskip.ogamemobile.adapter.game.ResultPage;
import pl.merskip.ogamemobile.adapter.game.pages.DefenseResult;
import pl.merskip.ogamemobile.adapter.game.pages.OverviewResult;
import pl.merskip.ogamemobile.adapter.game.pages.ResearchResult;
import pl.merskip.ogamemobile.adapter.game.pages.ResourcesResult;
import pl.merskip.ogamemobile.adapter.game.pages.ShipyardResult;
import pl.merskip.ogamemobile.adapter.game.pages.StationResult;

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
            default:
                throw new IllegalArgumentException("Unknown pageName: " + pageName);
        }
    }

}
