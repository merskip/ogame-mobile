package pl.merskip.ogamemobile.adapter;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Research;

/**
 * Test strony badań
 */
public class ResearchTest extends BuildItemsPage {

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Research(auth);
    }

    @Override
    protected boolean testItemsCount(int count) {
        return count == 16;
    }

    @Override
    protected String[] getAllowedIds() {
        return new String[] {
                "113", "120", "121", "114", "122", "115", "117", "118",
                "106", "108", "124", "123", "199", "109", "110", "111"
        };
    }
}
