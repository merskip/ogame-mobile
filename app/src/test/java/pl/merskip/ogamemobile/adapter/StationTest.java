package pl.merskip.ogamemobile.adapter;

/**
 * Test strony stacji
 */
public class StationTest extends BuildItemsPage {


    @Override
    protected String getPageName() {
        return "station";
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
