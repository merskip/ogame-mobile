package pl.merskip.ogamemobile.adapter.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Strona badań
 */
public class ResearchResult extends BuildItemsPage {

    @Override
    public List<BuildItem> onCreateResult() {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#base1 li"));
        appendFromListLi(buildItems, document.select("ul#base2 li"));
        appendFromListLi(buildItems, document.select("ul#base3 li"));
        appendFromListLi(buildItems, document.select("ul#base4 li"));

        return buildItems;
    }
}
