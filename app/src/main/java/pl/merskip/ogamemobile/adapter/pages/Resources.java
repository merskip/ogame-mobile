package pl.merskip.ogamemobile.adapter.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Strona zasobów planety
 */
public class Resources extends BuildItemsPage {

    @Override
    public List<BuildItem> onCreateResult() {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#building li"));
        appendFromListLi(buildItems, document.select("ul#storage li"));
        appendFromListLi(buildItems, document.select("ul#den li"));

        return buildItems;
    }

}
