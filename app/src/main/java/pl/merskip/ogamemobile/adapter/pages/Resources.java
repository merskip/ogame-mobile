package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Strona zasobów planety
 */
public class Resources extends BuildItemsPage {

    public Resources(AuthorizationData auth) {
        super(auth, "resources");
    }

    @Override
    public List<BuildItem> createResult(Document document) {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#building li"));
        appendFromListLi(buildItems, document.select("ul#storage li"));
        appendFromListLi(buildItems, document.select("ul#den li"));

        return buildItems;
    }

}
