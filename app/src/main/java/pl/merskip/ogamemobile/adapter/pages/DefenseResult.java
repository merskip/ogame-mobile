package pl.merskip.ogamemobile.adapter.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Strona obrony
 */
public class DefenseResult extends BuildItemsPage {

    @Override
    public List<BuildItem> onCreateResult() {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#defensebuilding li"));

        return buildItems;
    }
}
