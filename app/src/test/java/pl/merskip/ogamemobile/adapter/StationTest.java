package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.StationResult;

/**
 * Test strony stacji
 */
public class StationTest extends BuildItemsPage {

    @Override
    protected RequestPage<List<BuildItem>> createDownloadPage() {
        return new StationResult(auth);
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
