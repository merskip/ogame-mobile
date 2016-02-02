package pl.merskip.ogamemobile.adapter.game.pages;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.game.Building;

/**
 * Strona badań
 */
public class ResearchResult extends BuildingsResult {

    @Override
    public List<Building> onCreateResult() {
        List<Building> buildings = new ArrayList<>();

        appendFromListLi(buildings, document.select("ul#base1 li"));
        appendFromListLi(buildings, document.select("ul#base2 li"));
        appendFromListLi(buildings, document.select("ul#base3 li"));
        appendFromListLi(buildings, document.select("ul#base4 li"));

        return buildings;
    }
}
