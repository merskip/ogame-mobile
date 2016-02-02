package pl.merskip.ogamemobile.adapter.game.pages;

/**
 * Test strony stoczni
 */
public class ShipyardTest extends BuildItemsPage {


    @Override
    protected String getPageName() {
        return "shipyard";
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
