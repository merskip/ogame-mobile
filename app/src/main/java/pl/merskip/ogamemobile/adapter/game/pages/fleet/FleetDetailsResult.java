package pl.merskip.ogamemobile.adapter.game.pages.fleet;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;

import pl.merskip.ogamemobile.adapter.game.ResultPage;


public class FleetDetailsResult extends ResultPage<FleetDetailsResult.Set> {

    /**
     * Zestaw danych otrzymywanych przy pobieraniu formularzu wyboru statków
     */
    public static class Set
            extends FleetShipsResult.Set
            implements Serializable {

        public String union;
        public String acsValues;
    }

    private Set result;
    private Element form;

    @Override
    protected Set onCreateResult() {
        result = new Set();

        form = document.select("form[name=details]").first();
        appendValuesFromHiddenField();
        appendShips();
        appendCoords();
        appendSpeed();

        return result;
    }

    private void appendValuesFromHiddenField() {
        result.type = form.select("input[name=type]").val();
        result.mission = form.select("input[name=mission]").val();
        result.union = form.select("input[name=union]").val();
        result.acsValues = "-";
    }

    private void appendShips() {
        Elements inputs = form.select("input[name]");
        for (Element input : inputs) {
            if (input.attr("name").startsWith("am")) {
                FleetShipsResult.Ship ship = new FleetShipsResult.Ship();
                ship.id = input.attr("name").substring(2);
                ship.amount = Integer.parseInt(input.val());
                result.ships.add(ship);
            }
        }
    }

    private void appendCoords() {
        result.galaxy = form.getElementById("galaxy").val();
        result.system = form.getElementById("system").val();
        result.position = form.getElementById("position").val();
    }

    private void appendSpeed() {
        result.speed = form.getElementById("speed").val();
    }
}
