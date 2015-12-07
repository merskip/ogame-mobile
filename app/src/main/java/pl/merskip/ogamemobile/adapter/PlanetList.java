package pl.merskip.ogamemobile.adapter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * Lista planet
 */
public class PlanetList {

    public static List<Planet> fromDocument(Document document) {
        Elements planetsDiv = document.select("#planetList div");
        if (planetsDiv.size() == 0)
            return null;

        List<Planet> planetList = new LinkedList<>();
        for (Element div : planetsDiv) {
            planetList.add(fromPlanetDiv(div));
        }

        return planetList;
    }

    private static Planet fromPlanetDiv(Element planetDiv) {
        String id = planetDiv.id().replace("planet-", "");
        String name = planetDiv.select(".planet-name").text();
        String coordinate = planetDiv.select(".planet-koords").text();
        Planet planet = new Planet(id, name, coordinate);

        if (!planetDiv.select(".constructionIcon").isEmpty())
            planet.isBuild = true;
        if (!planetDiv.select(".alert").isEmpty())
            planet.isAlert = false;
        planet.iconUrl = planetDiv.select("img.planetPic").attr("src");

        return planet;
    }

}
