package pl.merskip.ogamemobile.adapter.game.pages;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.game.Building;

/**
 * Strona zasobów planety
 */
public class ResourcesResult extends BuildingsResult {

    @Override
    public List<Building> onCreateResult() {
        List<Building> buildings = new ArrayList<>();

        appendFromListLi(buildings, document.select("ul#building li"));
        appendFromListLi(buildings, document.select("ul#storage li"));
        appendFromListLi(buildings, document.select("ul#den li"));

        return buildings;
    }

}
