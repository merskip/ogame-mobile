package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Strona stacji
 */
public class Station extends BuildItemsPage {

    public Station(AuthorizationData auth) {
        super(auth, "station");
    }

    @Override
    public List<BuildItem> createResult(Document document) {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#stationbuilding li"));

        return buildItems;
    }
}
