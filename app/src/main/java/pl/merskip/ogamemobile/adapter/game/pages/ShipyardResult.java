package pl.merskip.ogamemobile.adapter.game.pages;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.game.Building;

/**
 * Strona stoczni
 */
public class ShipyardResult extends BuildingsResult {

    @Override
    public List<Building> onCreateResult() {
        List<Building> buildings = new ArrayList<>();

        appendFromListLi(buildings, document.select("ul#military li"));
        appendFromListLi(buildings, document.select("ul#civil li"));

        return buildings;
    }
}
