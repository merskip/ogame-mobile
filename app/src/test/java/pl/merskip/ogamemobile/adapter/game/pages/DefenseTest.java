package pl.merskip.ogamemobile.adapter.game.pages;

/**
 * Test strony obrony
 */
public class DefenseTest extends BuildItemsPage {

    @Override
    protected String getPageName() {
        return "defense";
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
