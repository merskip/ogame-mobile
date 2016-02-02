package pl.merskip.ogamemobile.adapter.game.pages;

import org.junit.Test;

import pl.merskip.ogamemobile.adapter.game.PageTest;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;

import static org.junit.Assert.assertNotEquals;

/**
 * Test strony podglądu
 */
public class OverviewTest extends PageTest {

    @Test
    public void testPage() throws Exception {
        RequestPage request = new RequestPage(auth, "overview");
        ResultPage result = new OverviewResult().createFromRequest(request);
        OverviewData data = (OverviewData) result.getResult();

        testPlanetInfo(data);
        testPlayerScore(data);
    }

    private void testPlanetInfo(OverviewData result) {
        assertNotEquals(null, result.planetDiameter);
        assertNotEquals(null, result.planetTemperature);
        assertNotEquals(null, result.planetCoordinate);

        System.out.println("Planet info:");
        System.out.println("\tdiameter=" + result.planetDiameter);
        System.out.println("\ttemperature=" + result.planetTemperature);
        System.out.println("\tcoordinate=" + result.planetCoordinate);
    }

    private void testPlayerScore(OverviewData result) {
        assertNotEquals(null, result.playerScore);
        assertNotEquals(null, result.playerPosition);
        assertNotEquals(null, result.playerHonor);

        System.out.println("Player score:");
        System.out.println("\tscore=" + result.playerScore);
        System.out.println("\tposition=" + result.playerPosition);
        System.out.println("\thonor=" + result.playerHonor);
    }
}
