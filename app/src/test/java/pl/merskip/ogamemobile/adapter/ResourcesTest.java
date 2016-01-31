package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Resources;

/**
 * Test strony zasobów
 */
public class ResourcesTest extends BuildItemsPage {

    @Override
    protected RequestPage<List<BuildItem>> createDownloadPage() {
        return new Resources(auth);
    }

    @Override
    protected boolean testItemsCount(int count) {
        return count == 9;
    }

    @Override
    protected String[] getAllowedIds() {
        return new String[] {
                "1", "2", "3", "4", "12",
                "212", "22", "23", "24"
        };
    }
}
