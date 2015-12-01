package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Strona badań
 */
public class Research extends BuildItemsPage {

    public Research(AuthorizationData auth) {
        super(auth, "research");
    }

    @Override
    public List<BuildItem> createResult(Document document) {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#base1 li"));
        appendFromListLi(buildItems, document.select("ul#base2 li"));
        appendFromListLi(buildItems, document.select("ul#base3 li"));
        appendFromListLi(buildItems, document.select("ul#base4 li"));

        return buildItems;
    }
}
