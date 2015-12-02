package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Station;

/**
 * Test strony stacji
 */
public class StationTest extends BuildItemsPage {

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Station(auth);
    }

    @Override
    protected boolean testItemsCount(int count) {
        return count == 7 || count == 5; // Planeta lub księżyc
    }

    @Override
    protected String[] getAllowedIds() {
        return new String[] {
                "14", "21", "31", "34", "44",
                "15", "33", "41", "42", "43"
        };
    }
}
