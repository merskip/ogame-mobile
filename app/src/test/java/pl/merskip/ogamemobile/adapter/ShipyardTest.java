package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Shipyard;

/**
 * Test strony stoczni
 */
public class ShipyardTest extends BuildItemsPage {

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Shipyard(auth);
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
