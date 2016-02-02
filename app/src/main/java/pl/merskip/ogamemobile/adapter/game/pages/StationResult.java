package pl.merskip.ogamemobile.adapter.game.pages;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.game.Building;

/**
 * Strona stacji
 */
public class StationResult extends BuildingsResult {

    @Override
    public List<Building> onCreateResult() {
        List<Building> buildings = new ArrayList<>();

        appendFromListLi(buildings, document.select("ul#stationbuilding li"));

        return buildings;
    }
}
