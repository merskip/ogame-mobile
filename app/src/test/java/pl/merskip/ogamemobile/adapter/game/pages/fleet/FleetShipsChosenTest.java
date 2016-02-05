package pl.merskip.ogamemobile.adapter.game.pages.fleet;


import org.junit.Test;

import pl.merskip.ogamemobile.adapter.game.PageTest;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;

public class FleetShipsChosenTest extends PageTest {

    @Test
    public void testPage() throws Exception {
        RequestPage request = new RequestPage(auth, "fleet1");
        ResultPage result = new FleetShipsChosenResult().createFromRequest(request);
        FleetShipsChosenResult.Set set =
                (FleetShipsChosenResult.Set) result.createFromRequest(request).getResult();

        System.out.printf("Default values: \n");
        System.out.printf(" - coords: [%s:%s:%s]\n", set.galaxy, set.system, set.position);
        System.out.printf(" - type: %s\n", set.type);
        System.out.printf(" - mission: %s\n", set.mission);
        System.out.printf(" - speed: %s0%%\n", set.speed);

        System.out.printf(" - ships max:\n");
        for (FleetShipsChosenResult.Ship ship : set.shipsMax) {
            System.out.printf("   * id=%s, max=%d, name=%s\n",
                    ship.id, ship.max, ship.name);
        }
    }
}
