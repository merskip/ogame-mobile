package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.DefenseResult;

/**
 * Test strony obrony
 */
public class DefenceTest extends BuildItemsPage {

    @Override
    protected RequestPage<List<BuildItem>> createDownloadPage() {
        return new DefenseResult(auth);
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
