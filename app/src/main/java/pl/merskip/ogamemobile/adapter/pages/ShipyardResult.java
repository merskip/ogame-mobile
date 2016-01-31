package pl.merskip.ogamemobile.adapter.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Strona stoczni
 */
public class ShipyardResult extends BuildItemsPage {

    @Override
    public List<BuildItem> onCreateResult() {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#military li"));
        appendFromListLi(buildItems, document.select("ul#civil li"));

        return buildItems;
    }
}
