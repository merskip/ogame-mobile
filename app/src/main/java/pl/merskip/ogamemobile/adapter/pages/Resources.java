package pl.merskip.ogamemobile.adapter.pages;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.pages.BuildItem.BuildState;

/**
 * Strona zasobów planety
 */
public class Resources extends AbstractPage<List<BuildItem>> {

    public Resources(AuthorizationData auth) {
        super(auth, "resources");
    }

    @Override
    public List<BuildItem> createResult(Document document) {
        List<BuildItem> buildItems = new ArrayList<>();

        appendFromListL(buildItems, document.select("ul#building li"));
        appendFromListL(buildItems, document.select("ul#storage li"));
        appendFromListL(buildItems, document.select("ul#den li"));

        return buildItems;
    }

    private void appendFromListL(List<BuildItem> buildItems, Elements elementsLi) {
        for (Element element : elementsLi) {
            BuildItem buildItem = createFromLi(element);
            buildItems.add(buildItem);
        }
    }

    private BuildItem createFromLi(Element li) {
        String id = li.select(".tooltip").attr("ref");
        String name = li.select(".textlabel").text().trim();
        String levelWithNameAndDots = li.select(".level").text();
        String level = levelWithNameAndDots.replace(name, "").replace(".", "").trim();

        BuildItem buildItem = new BuildItem(id, name, Integer.parseInt(level));
        buildItem.buildState = getBuildSate(li);

        /* Gdy budynek jest rozbudowywany,
         * nazwa budynku i poziom są napisane w innym miejscu */
        if (buildItem.buildState == BuildState.Upgrading) {
            buildItem.name = li.select(".tooltip").attr("title");
            String levelWithDots = li.select(".ecke .level").text();
            level = levelWithDots.replace(".", "").trim();
            buildItem.level = Integer.parseInt(level);
        }

        return buildItem;
    }

    private static BuildState getBuildSate(Element li) {
        if (!li.select(".construction").isEmpty())
            return BuildState.Upgrading;

        if (li.hasClass("on"))
            return BuildState.ReadyToBuild;
        else if (li.hasClass("disabled"))
            return BuildState.TooFewResources;
        else if (li.hasClass("off"))
            return BuildState.UnmetRequirements;
        else
            Log.e("BuildItem", "Unknown building state: " + li.className());
        return null;
    }

}
