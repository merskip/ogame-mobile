package pl.merskip.ogamemobile.adapter.game.pages;


import org.jsoup.nodes.Document;
import org.junit.Test;

import pl.merskip.ogamemobile.adapter.game.PageTest;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetDetailsRequest;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetDetailsResult;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsRequest;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsResult;

public class FleetTest extends PageTest {

    @Test
    public void testFleet() throws Exception {
        System.out.println("\n === Ships ===");
        FleetShipsResult.Set ships = downloadShips();
        printShips(ships);

        // Wybranie jednego statku pierwszego z listy
        ships.ships.get(0).amount = 1;

        System.out.println("\n === Details ===");
        FleetDetailsResult.Set details = downloadDetails(ships);
        printDetails(details);
    }

    private FleetShipsResult.Set downloadShips() throws Exception {
        RequestPage request = new FleetShipsRequest(auth);
        Document document = request.download();
        return new FleetShipsResult().createResult(document, request);
    }

    private void printShips(FleetShipsResult.Set result) {
        System.out.printf(" - *coords: [%s:%s:%s]\n", result.galaxy, result.system, result.position);
        System.out.printf(" - *type: %s\n", result.type);
        System.out.printf(" - *mission: %s\n", result.mission);
        System.out.printf(" - *speed: %s0%%\n", result.speed);

        System.out.printf(" - ships max:\n");
        for (FleetShipsResult.Ship ship : result.ships) {
            System.out.printf("   * id=%s, max=%d, name=%s\n",
                    ship.id, ship.max, ship.name);
        }
    }

    private FleetDetailsResult.Set downloadDetails(FleetShipsResult.Set ships)
            throws Exception {
        RequestPage request = new FleetDetailsRequest(auth, ships);
        Document document = request.download();
        return new FleetDetailsResult().createResult(document, request);
    }

    private void printDetails(FleetDetailsResult.Set result) {
        System.out.printf(" - *mission: %s\n", result.mission);
        System.out.printf(" - *union: %s\n", result.union);
        System.out.printf(" - *type: %s\n", result.type);

        System.out.printf(" - coords: [%s:%s:%s]\n", result.galaxy, result.system, result.position);
        System.out.printf(" - acsValues: %s\n", result.acsValues);
        System.out.printf(" - speed: %s\n", result.speed);

        System.out.printf(" - Ships:");
        for (FleetShipsResult.Ship ship : result.ships) {
            System.out.printf(" am%s=%d", ship.id, ship.amount);
        }
        System.out.printf("\n");
    }

}
