package pl.merskip.ogamemobile.adapter.game.pages;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.game.BuildItem;

/**
 * Strona zasobów planety
 */
public class ResourcesResult extends BuildItemsPage {

    @Override
    public List<BuildItem> onCreateResult() {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#building li"));
        appendFromListLi(buildItems, document.select("ul#storage li"));
        appendFromListLi(buildItems, document.select("ul#den li"));

        return buildItems;
    }

}
