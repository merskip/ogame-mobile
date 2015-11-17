package pl.merskip.ogamemobile.adapter;

import org.junit.Test;

import pl.merskip.ogamemobile.adapter.pages.Overview;

import static org.junit.Assert.assertNotEquals;

/**
 * Test strony podglądu
 */
public class OverviewTest extends PageTest {

    @Test
    public void testPage() throws Exception {
        Overview page = new Overview(auth);

        Overview.Data result = page.download();

        testPlanetInfo(result);
        testPlayerScore(result);
    }

    private void testPlanetInfo(Overview.Data result) {
        Overview.PlanetInfo planetInfo = result.planetInfo;
        assertNotEquals(null, planetInfo.diameter);
        assertNotEquals(null, planetInfo.temperature);
        assertNotEquals(null, planetInfo.coordinate);

        System.out.println("Planet info:");
        System.out.println("\tdiameter=" + planetInfo.diameter);
        System.out.println("\ttemperature=" + planetInfo.temperature);
        System.out.println("\tcoordinate=" + planetInfo.coordinate);
    }

    private void testPlayerScore(Overview.Data result) {
        Overview.PlayerScore playerScore = result.playerScore;
        assertNotEquals(null, playerScore.score);
        assertNotEquals(null, playerScore.position);
        assertNotEquals(null, playerScore.honor);

        System.out.println("Player score:");
        System.out.println("\tscore=" + playerScore.score);
        System.out.println("\tposition=" + playerScore.position);
        System.out.println("\thonor=" + playerScore.honor);
    }
}
