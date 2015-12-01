package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Strona obrony
 */
public class Defense extends BuildItemsPage {

    public Defense(AuthorizationData auth) {
        super(auth, "defense");
    }

    @Override
    public List<BuildItem> createResult(Document document) {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListLi(buildItems, document.select("ul#defensebuilding li"));

        return buildItems;
    }
}
