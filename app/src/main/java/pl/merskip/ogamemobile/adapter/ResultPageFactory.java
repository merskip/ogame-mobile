package pl.merskip.ogamemobile.adapter;

import pl.merskip.ogamemobile.adapter.pages.DefenseResult;
import pl.merskip.ogamemobile.adapter.pages.OverviewResult;
import pl.merskip.ogamemobile.adapter.pages.ResearchResult;
import pl.merskip.ogamemobile.adapter.pages.ResourcesResult;
import pl.merskip.ogamemobile.adapter.pages.ResultPage;
import pl.merskip.ogamemobile.adapter.pages.ShipyardResult;
import pl.merskip.ogamemobile.adapter.pages.StationResult;

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
