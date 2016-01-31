package pl.merskip.ogamemobile.adapter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.RequestPage;

/**
 * Testowanie listy planet
 */
public class PlanetListTest extends PageTest {

    @Test
    public void testPlanetListOnline() throws Exception {
        RequestPage<?> examplePage = getExamplePage();
        Document document = examplePage.getDownloadedDocument();
        testFromDocument(document);
    }

    @Test
    public void testPlanetListWithMoons() throws Exception {
        Document document = getExampleDocumentFromAssets();
        Assert.assertNotNull(document);
        testFromDocument(document);
    }

    private void testFromDocument(Document document) throws Exception {
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

            if (planet.moon != null) {
                System.out.printf("\t * moon: id=%s, coords=%s\n",
                        planet.moon.id, planet.moon.coordinate);

                Assert.assertNotNull(planet.moon.id);
                Assert.assertNotNull(planet.moon.coordinate);
                Assert.assertNotNull(planet.moon.iconUrl);

                Assert.assertNotEquals("", planet.moon.id);
                Assert.assertEquals(planet.moon.coordinate, planet.moon.coordinate);
                Assert.assertNotEquals("", planet.moon.iconUrl);
            }
        }
    }

}
