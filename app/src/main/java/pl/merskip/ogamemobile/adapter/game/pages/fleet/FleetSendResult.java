package pl.merskip.ogamemobile.adapter.game.pages.fleet;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;

import pl.merskip.ogamemobile.adapter.game.ResultPage;


public class FleetSendResult extends ResultPage<FleetSendResult.Set> {

    /**
     * Zestaw danych otrzymywanych przy pobieraniu formularzu wyboru statków
     */
    public static class Set
            extends FleetDetailsResult.Set
            implements Serializable {

        public String token;
        public String union2;
        public String capacity;
        public String holdingTime;
        public String expeditionTime;
        public String metal;
        public String crystal;
        public String deuterium;
    }

    private Set result;
    private Element form;

    @Override
    protected Set onCreateResult() {
        result = new Set();

        form = document.select("form[name=sendForm]").first();
        appendValuesFromHiddenField();
        appendShips();
        appendResources();

        return result;
    }

    private void appendValuesFromHiddenField() {
        result.token = form.select("input[name=token]").val();

        result.galaxy = form.select("input[name=galaxy]").val();
        result.system = form.select("input[name=system]").val();
        result.position = form.select("input[name=position]").val();

        result.speed = form.select("input[name=speed").val();
        result.type = form.select("input[name=type]").val();
        result.union2 = form.select("input[name=union2]").val();
        result.acsValues = form.select("input[name=acsValues]").val();

        result.mission = form.select("input[name=mission]").val();
        result.holdingTime = "1";
        result.expeditionTime = "1";
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

    private void appendResources() {
        result.metal = form.getElementById("metal").val();
        result.crystal = form.getElementById("crystal").val();
        result.deuterium = form.getElementById("deuterium").val();

        result.capacity = form.getElementById("loadRoom")
                .select(".bar_container")
                .attr("data-capacity");
    }

}
