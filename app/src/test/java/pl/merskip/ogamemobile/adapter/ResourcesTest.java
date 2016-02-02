package pl.merskip.ogamemobile.adapter;

/**
 * Test strony zasobów
 */
public class ResourcesTest extends BuildItemsPage {


    @Override
    protected String getPageName() {
        return "resources";
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
