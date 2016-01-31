package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.ShipyardResult;

/**
 * Test strony stoczni
 */
public class ShipyardTest extends BuildItemsPage {

    @Override
    protected RequestPage<List<BuildItem>> createDownloadPage() {
        return new ShipyardResult(auth);
    }

    @Override
    protected boolean testItemsCount(int count) {
        return count == 14;
    }

    @Override
    protected String[] getAllowedIds() {
        return new String[]{
                "204", "205", "206", "207", "202", "203", "208",
                "215", "211", "213", "214", "209", "210", "212"
        };
    }
}
