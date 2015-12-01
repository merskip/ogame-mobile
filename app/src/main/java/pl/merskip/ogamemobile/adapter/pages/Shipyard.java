package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Strona stoczni
 */
public class Shipyard extends BuildItemsPage {

    public Shipyard(AuthorizationData auth) {
        super(auth, "shipyard");
    }

    @Override
    public List<BuildItem> createResult(Document document) {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#military li"));
        appendFromListLi(buildItems, document.select("ul#civil li"));

        return buildItems;
    }
}
