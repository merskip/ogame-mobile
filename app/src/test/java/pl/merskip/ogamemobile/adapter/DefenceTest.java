package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Defense;

/**
 * Test strony obrony
 */
public class DefenceTest extends BuildItemsPage {

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Defense(auth);
    }

    @Override
    protected boolean testItemsCount(int count) {
        return count == 10;
    }

    @Override
    protected String[] getAllowedIds() {
        return new String[]{
                "401", "402", "403", "404", "405",
                "406", "407", "408", "502", "503"
        };
    }
}
