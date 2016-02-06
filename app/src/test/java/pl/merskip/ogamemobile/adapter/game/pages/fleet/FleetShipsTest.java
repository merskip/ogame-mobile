package pl.merskip.ogamemobile.adapter.game.pages.fleet;


import org.jsoup.nodes.Document;
import org.junit.Test;

import pl.merskip.ogamemobile.adapter.game.PageTest;
import pl.merskip.ogamemobile.adapter.game.RequestPage;

public class FleetShipsTest extends PageTest {

    @Test
    public void testPage() throws Exception {
        RequestPage request = new RequestPage(auth, "fleet1");
        Document document = request.download();
        FleetShipsResult.Set result = new FleetShipsResult().createResult(document, request);

        System.out.printf("Default values: \n");
        System.out.printf(" - coords: [%s:%s:%s]\n", result.galaxy, result.system, result.position);
        System.out.printf(" - type: %s\n", result.type);
        System.out.printf(" - mission: %s\n", result.mission);
        System.out.printf(" - speed: %s0%%\n", result.speed);

        System.out.printf(" - ships max:\n");
        for (FleetShipsResult.Ship ship : result.shipsMax) {
            System.out.printf("   * id=%s, max=%d, name=%s\n",
                    ship.id, ship.max, ship.name);
        }
    }
}
