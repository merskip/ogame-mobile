package pl.merskip.ogamemobile.adapter.game.pages;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.game.Building;

/**
 * Strona obrony
 */
public class DefenseResult extends BuildingsResult {

    @Override
    public List<Building> onCreateResult() {
        List<Building> buildings = new ArrayList<>();

        appendFromListLi(buildings, document.select("ul#defensebuilding li"));

        return buildings;
    }
}
