package pl.merskip.ogamemobile.adapter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;

/**
 * Testowanie listy planet
 */
public class PlanetListTest extends PageTest {

    @Test
    public void testPlanetList() throws Exception {
        AbstractPage<?> examplePage = getExamplePage();
        Document document = examplePage.getDocument();
        Assert.assertNotNull(document);

        List<Planet> planetList = PlanetList.fromDocument(document);

        Assert.assertNotNull(planetList);
        Assert.assertNotEquals(0, planetList.size());

        System.out.println("\nPlanet list:");
        for (Planet planet : planetList) {
            System.out.printf(" - id=%s, name=%s, coords=%s\n",
                    planet.id, planet.name, planet.coordinate);

            Assert.assertNotNull(planet.id);
            Assert.assertNotNull(planet.name);
            Assert.assertNotNull(planet.coordinate);
            Assert.assertNotNull(planet.iconUrl);

            Assert.assertTrue(StringUtils.isNumeric(planet.id));
            Assert.assertNotEquals("", planet.name);
            Assert.assertTrue(planet.coordinate.matches("^\\[\\d+:\\d+:\\d+\\]$"));
            Assert.assertNotEquals("", planet.iconUrl);
        }
    }

}
