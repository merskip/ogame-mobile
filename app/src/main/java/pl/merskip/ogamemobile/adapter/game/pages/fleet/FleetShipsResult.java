package pl.merskip.ogamemobile.adapter.game.pages.fleet;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.merskip.ogamemobile.adapter.game.ResultPage;

/**
 * Żądanie strony z listą statków do wyboru
 */
public class FleetShipsResult extends ResultPage<FleetShipsResult.Set> {

    /**
     * Zestaw danych otrzymywanych przy pobieraniu formularzu wyboru statków
     */
    public class Set implements Serializable {
        public String galaxy;
        public String system;
        public String position;
        public String type;
        public String mission;
        public String speed;

        public List<Ship> ships = new ArrayList<>();
    }

    public static class Ship {
        public String id;
        public String name;
        public int amount = 0;
        public int max;
    }

    private Set result;
    private Element form;

    @Override
    protected Set onCreateResult() {
        result = new Set();

        form = document.select("#shipsChosen").first();
        appendValuesFromHiddenField();
        appendChipsMax();

        return result;
    }

    private void appendValuesFromHiddenField() {
        result.galaxy = form.select("input[name=galaxy]").val();
        result.system = form.select("input[name=system]").val();
        result.position = form.select("input[name=position]").val();
        result.type = form.select("input[name=type]").val();
        result.mission = form.select("input[name=mission]").val();
        result.speed = form.select("input[name=speed]").val();
    }

    private void appendChipsMax() {
        Element sendAllButton = document.select("#sendall").first();
        String onclick = sendAllButton.attr("onclick");

        String regex = "\"(\\d+)\":(\\d+),?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(onclick);
        while (matcher.find()) {
            Ship ship = new Ship();
            ship.id = matcher.group(1);
            ship.max = Integer.parseInt(matcher.group(2));
            ship.name = getShipNameById(ship.id);

            result.ships.add(ship);
        }
    }

    private String getShipNameById(String id) {
        Element shipElement = form.getElementById("button" + id);
        String name = shipElement.select(".textlabel").text();
        return name.trim();
    }
}
