package pl.merskip.ogamemobile.adapter.game.pages;

/**
 * Test strony badań
 */
public class ResearchTest extends BuildingsPage {


    @Override
    protected String getPageName() {
        return "research";
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
