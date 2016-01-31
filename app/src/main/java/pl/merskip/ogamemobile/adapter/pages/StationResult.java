package pl.merskip.ogamemobile.adapter.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Strona stacji
 */
public class StationResult extends BuildItemsPage {

    @Override
    public List<BuildItem> onCreateResult() {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#stationbuilding li"));

        return buildItems;
    }
}
